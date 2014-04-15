package me.burnblader.npctesting;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.server.v1_7_R1.DataWatcher;
import net.minecraft.server.v1_7_R1.EntityHuman;
import net.minecraft.server.v1_7_R1.PacketPlayOutAnimation;
import net.minecraft.server.v1_7_R1.PacketPlayOutBed;
import net.minecraft.server.v1_7_R1.PacketPlayOutEntity;
import net.minecraft.server.v1_7_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R1.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_7_R1.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_7_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_7_R1.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_7_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_7_R1.PacketPlayOutRelEntityMoveLook;
import net.minecraft.util.com.mojang.authlib.GameProfile;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
 
    public class Human {
 
    String name;
    World world;
    public int id;
    Location l;
    int itemInHand;
 
    private List<Integer> ids = new ArrayList<Integer>();
 
    private void setPrivateField(@SuppressWarnings("rawtypes") Class type, Object object, String name, Object value) {
    try {
    Field f = type.getDeclaredField(name);
    f.setAccessible(true);
    f.set(object, value);
    f.setAccessible(false);
    } catch (Exception ex) {
    ex.printStackTrace();
    }
    }
 
    public void setPitch(float pitch) {
    this.walk(0.0d, 0.0d, 0.0d, l.getYaw(), pitch);
    }
 
    public void setYaw(float yaw) {
    this.walk(0.0d, 0.0d, 0.0d, yaw, l.getPitch());
    }
 
    public Human(World w, String name, int id, Location l, int itemInHand) {
    this.name = name;
    this.world = w;
    this.id = id;
    this.l = l;
    this.itemInHand = itemInHand;
    DataWatcher d = new DataWatcher(null);
    d.a(0, (Object) (byte) 0);
    d.a(1, (Object) (short) 0);
    d.a(8, (Object) (byte) 0);
    PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn();
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "a", id);
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "b", new GameProfile("", name));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "c", ((int) l.getX() * 32));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "d", ((int) l.getY() * 32));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "e", ((int) l.getZ() * 32));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "f", getCompressedAngle(l.getYaw()));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "g", getCompressedAngle(l.getPitch()));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "h", itemInHand);
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "i", d);
 
    PacketPlayOutEntityTeleport tp = new PacketPlayOutEntityTeleport();
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "a", id);
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "b", ((int) l.getX() * 32));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "c", ((int) l.getY() * 32));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "d", ((int) l.getZ() * 32));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "e", getCompressedAngle(l.getYaw()));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "f", getCompressedAngle(l.getPitch()));
 
    for (Player p : Bukkit.getOnlinePlayers()) {
    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(spawn);
    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(tp);
    }
    ids.add(id);
    }
 
    public Human(EntityHuman h) {
    PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(h);
    int id = new Random().nextInt(5000 - 1000) + 1000;
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "a", id);
    this.id = id;
    PacketPlayOutEntityEquipment armor1 = new PacketPlayOutEntityEquipment(id, 1, h.inventory.getArmorContents()[0]);
    PacketPlayOutEntityEquipment armor2 = new PacketPlayOutEntityEquipment(id, 2, h.inventory.getArmorContents()[1]);
    PacketPlayOutEntityEquipment armor3 = new PacketPlayOutEntityEquipment(id, 3, h.inventory.getArmorContents()[2]);
    PacketPlayOutEntityEquipment armor4 = new PacketPlayOutEntityEquipment(id, 4, h.inventory.getArmorContents()[3]);
    PacketPlayOutEntityEquipment sword = new PacketPlayOutEntityEquipment(id, 0, h.inventory.getItem(0));
    for (Player p : Bukkit.getOnlinePlayers()) {
    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(spawn);
    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(armor1);
    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(armor2);
    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(armor3);
    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(armor4);
    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(sword);
    }
    }
 
    public void teleport(Location loc) {
    PacketPlayOutEntityTeleport tp = new PacketPlayOutEntityTeleport();
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "a", id);
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "b", ((int) (loc.getX() * 32)));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "c", ((int) (loc.getY() * 32)));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "d", ((int) (loc.getZ() * 32)));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "e", getCompressedAngle(loc.getYaw()));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "f", getCompressedAngle(loc.getPitch()));
    this.l = loc;
    for (Player p : Bukkit.getOnlinePlayers()) {
    ((CraftPlayer)p).getHandle().playerConnection.sendPacket(tp);
    }
    }
 
    private byte getCompressedAngle(float value) {
    return (byte) ((value * 256.0F) / 360.0F);
    }
 
    private byte getCompressedAngle2(float value) {
    return (byte) ((value * 256.0F) / 360.0F);
    }
 
    public void remove() {
    PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(id);
    for (Player p : Bukkit.getOnlinePlayers()) {
    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }
    }
 
    public void updateItems(ItemStack hand, ItemStack boots, ItemStack legs, ItemStack chest, ItemStack helmet) {
 
    PacketPlayOutEntityEquipment[] ps = new PacketPlayOutEntityEquipment[]{
    new PacketPlayOutEntityEquipment(id, 1, CraftItemStack.asNMSCopy(boots)),
    new PacketPlayOutEntityEquipment(id, 2, CraftItemStack.asNMSCopy(legs)),
    new PacketPlayOutEntityEquipment(id, 3, CraftItemStack.asNMSCopy(chest)),
    new PacketPlayOutEntityEquipment(id, 4, CraftItemStack.asNMSCopy(helmet)),
    new PacketPlayOutEntityEquipment(id, 0, CraftItemStack.asNMSCopy(hand))
    };
    for (PacketPlayOutEntityEquipment pack : ps) {
    for (Player p : Bukkit.getOnlinePlayers()) {
    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(pack);
    }
    }
    }
 
    @Deprecated
    public void setName(String s) {
    DataWatcher d = new DataWatcher(null);
    d.a(0, (Object) (byte) 0);
    d.a(1, (Object) (short) 0);
    d.a(8, (Object) (byte) 0);
    d.a(10, (Object) (String) s);
    //d.a(11, (Object) (byte) 0);
    PacketPlayOutEntityMetadata packet40 = new PacketPlayOutEntityMetadata(id, d, true);
    for (Player p : Bukkit.getOnlinePlayers()) {
    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet40);
    }
    }
 
    public void hideForPlayer(Player p) {
    DataWatcher d = new DataWatcher(null);
    d.a(0, (Object) (byte) 32);
    d.a(1, (Object) (short) 0);
    d.a(8, (Object) (byte) 0);
    PacketPlayOutEntityMetadata packet40 = new PacketPlayOutEntityMetadata(id, d, true);
    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet40);
    }
 
    public void showForPlayer(Player p) {
    DataWatcher d = new DataWatcher(null);
    d.a(0, (Object) (byte) 0);
    d.a(1, (Object) (short) 0);
    d.a(8, (Object) (byte) 0);
    PacketPlayOutEntityMetadata packet40 = new PacketPlayOutEntityMetadata(id, d, true);
    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet40);
    }
 
    public void addPotionColor(Color r) {
    int color = r.asBGR();
    final DataWatcher dw = new DataWatcher(null);
    dw.a(7, Integer.valueOf(color));
    PacketPlayOutEntityMetadata packet40 = new PacketPlayOutEntityMetadata(id, dw, true);
    for (Player p : Bukkit.getOnlinePlayers()) {
    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet40);
    }
    }
 
    public void addPotionColor(int color) {
    final DataWatcher dw = new DataWatcher(null);
    dw.a(7, Integer.valueOf(color));
    PacketPlayOutEntityMetadata packet40 = new PacketPlayOutEntityMetadata(id, dw, true);
    for (Player p : Bukkit.getOnlinePlayers()) {
    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet40);
    }
    }
 
    public void walk(double a, double b, double c) {
    walk(a, b, c, l.getYaw(), l.getPitch());
    }
 
    public void walk(double a, double b, double c, float yaw, float pitch) {
    byte x = (byte) a;
    byte y = (byte) b;
    byte z = (byte) c;
    PacketPlayOutRelEntityMoveLook packet = new PacketPlayOutRelEntityMoveLook();
    setPrivateField(PacketPlayOutEntity.class, packet, "a", id);
    setPrivateField(PacketPlayOutEntity.class, packet, "b", x);
    setPrivateField(PacketPlayOutEntity.class, packet, "c", y);
    setPrivateField(PacketPlayOutEntity.class, packet, "d", z);
    setPrivateField(PacketPlayOutEntity.class, packet, "e", getCompressedAngle(yaw));
    setPrivateField(PacketPlayOutEntity.class, packet, "f", getCompressedAngle2(pitch));
 
    PacketPlayOutEntityHeadRotation p2 = new PacketPlayOutEntityHeadRotation();
    setPrivateField(PacketPlayOutEntityHeadRotation.class, p2, "a", id);
    setPrivateField(PacketPlayOutEntityHeadRotation.class, p2, "b", getCompressedAngle(yaw));
 
    for (Player p : Bukkit.getOnlinePlayers()) {
    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(p2);
    }
    l.setPitch(pitch);
    l.setYaw(yaw);
    l.add(a, b, c);
    }
 
    public void sendtoplayer(Player who) {
    DataWatcher d = new DataWatcher(null);
    d.a(0, (Object) (byte) 0);
    d.a(1, (Object) (short) 0);
    d.a(8, (Object) (byte) 0);
    PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn();
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "a", id);
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "b", new GameProfile("", name));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "c", ((int) (l.getX() * 32)));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "d", ((int) (l.getY() * 32)));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "e", ((int) (l.getZ() * 32)));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "f", getCompressedAngle(l.getYaw()));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "g", getCompressedAngle(l.getPitch()));
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "h", itemInHand);
    setPrivateField(PacketPlayOutNamedEntitySpawn.class, spawn, "i", d);
 
    PacketPlayOutEntityTeleport tp = new PacketPlayOutEntityTeleport();
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "a", id);
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "b", ((int) (l.getX() * 32)));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "c", ((int) (l.getY() * 32)));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "d", ((int) (l.getZ() * 32)));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "e", getCompressedAngle(l.getYaw()));
    setPrivateField(PacketPlayOutEntityTeleport.class, tp, "f", getCompressedAngle(l.getPitch()));
 
    ((CraftPlayer) who).getHandle().playerConnection.sendPacket(spawn);
    ((CraftPlayer) who).getHandle().playerConnection.sendPacket(tp);
    }
 
    public void setInvisible() {
    DataWatcher d = new DataWatcher(null);
    d.a(0, (Object) (byte) 32);
    d.a(1, (Object) (short) 0);
    d.a(8, (Object) (byte) 0);
    PacketPlayOutEntityMetadata packet40 = new PacketPlayOutEntityMetadata(id, d, true);
    for (Player p : Bukkit.getOnlinePlayers()) {
    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet40);
    }
    }
 
    public void setCrouched() {
    DataWatcher d = new DataWatcher(null);
    d.a(0, (Object) (byte) 2);
    d.a(1, (Object) (short) 0);
    d.a(8, (Object) (byte) 0);
    PacketPlayOutEntityMetadata packet40 = new PacketPlayOutEntityMetadata(id, d, true);
    for (Player p : Bukkit.getOnlinePlayers()) {
    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet40);
    }
    }
 
    public void reset() {
    DataWatcher d = new DataWatcher(null);
    d.a(0, (Object) (byte) 0);
    d.a(1, (Object) (short) 0);
    d.a(8, (Object) (byte) 0);
    PacketPlayOutEntityMetadata packet40 = new PacketPlayOutEntityMetadata(id, d, true);
    for (Player p : Bukkit.getOnlinePlayers()) {
    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet40);
    }
    }
 
    public void sprint() {
    DataWatcher d = new DataWatcher(null);
    d.a(0, (Object) (byte) 8);
    d.a(1, (Object) (short) 0);
    d.a(8, (Object) (byte) 0);
    PacketPlayOutEntityMetadata packet40 = new PacketPlayOutEntityMetadata(id, d, true);
    for (Player p : Bukkit.getOnlinePlayers()) {
    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet40);
    }
    }
 
    @Deprecated
    public void block() {
    DataWatcher d = new DataWatcher(null);
    d.a(0, (Object) (byte) 16);
    d.a(1, (Object) (short) 0);
    d.a(6, (Object) (byte) 0);
    PacketPlayOutEntityMetadata packet40 = new PacketPlayOutEntityMetadata(id, d, true);
    for (Player p : Bukkit.getOnlinePlayers()) {
    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet40);
    }
    }
 
    public void damage() {
    PacketPlayOutAnimation packet18 = new PacketPlayOutAnimation();
    setPrivateField(PacketPlayOutAnimation.class, packet18, "a", id);
    setPrivateField(PacketPlayOutAnimation.class, packet18, "b", 2);
    for (Player p : Bukkit.getOnlinePlayers()) {
    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet18);
    }
    }
 
    public void swingArm() {
    PacketPlayOutAnimation packet18 = new PacketPlayOutAnimation();
    setPrivateField(PacketPlayOutAnimation.class, packet18, "a", id);
    setPrivateField(PacketPlayOutAnimation.class, packet18, "b", 0);
    for (Player p : Bukkit.getOnlinePlayers()) {
    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet18);
    }
    }
 
    @Deprecated
    public void eatInHand() {
    PacketPlayOutAnimation packet18 = new PacketPlayOutAnimation();
    setPrivateField(PacketPlayOutAnimation.class, packet18, "a", id);
    setPrivateField(PacketPlayOutAnimation.class, packet18, "b", 5);
    for (Player p : Bukkit.getOnlinePlayers()) {
    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet18);
    }
    }
 
    public void sleep(Location loc) {
 
    PacketPlayOutBed packet17 = new PacketPlayOutBed();
    setPrivateField(PacketPlayOutBed.class, packet17, "a", id);
    setPrivateField(PacketPlayOutBed.class, packet17, "b", (int) loc.getX());
    setPrivateField(PacketPlayOutBed.class, packet17, "c", (int) loc.getY());
    setPrivateField(PacketPlayOutBed.class, packet17, "d", (int) loc.getZ());
  // setPrivateField(PacketPlayOutBed.class, packet17, "e", 0);
 
    for (Player p : Bukkit.getOnlinePlayers()){
    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet17);
    }
 
 
    }
 
    public double getX() {
    return l.getX();
    }
 
    public double getY() {
    return l.getY();
    }
 
    public double getZ() {
    return l.getZ();
    }
 
    public Location getLocation() {
    return l;
    }
 
    }