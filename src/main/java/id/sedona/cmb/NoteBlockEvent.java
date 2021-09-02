package id.sedona.cmb;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class NoteBlockEvent implements Listener {
    private final List<Material> REPLACE = Arrays.asList(Material.AIR, Material.CAVE_AIR, Material.VOID_AIR,
            Material.GRASS, Material.SEAGRASS, Material.WATER, Material.LAVA);
    private final List<UUID> antiFastPlace = new ArrayList<>();


    public void updateAndCheck(@NotNull Location loc) {
        Block block = loc.getBlock().getRelative(BlockFace.UP);
        if (block.getType() == Material.NOTE_BLOCK)
            block.getState().update(true, true);
        Block nextBlock = block.getRelative(BlockFace.UP);
        if (nextBlock.getType() == Material.NOTE_BLOCK) {
            updateAndCheck(block.getLocation());
        }
    }


    @EventHandler(ignoreCancelled = true)
    private void onBLockPhysics(@NotNull BlockPhysicsEvent event) {
        Block block = event.getBlock(),
                topBlock = block.getRelative(BlockFace.UP),
                bottomBlock = block.getRelative(BlockFace.DOWN);

        if (topBlock.getType() == Material.NOTE_BLOCK) {
            updateAndCheck(block.getLocation());
            if (Tag.DOORS.isTagged(block.getType()) && block.getBlockData() instanceof Door) {
                Door data = (Door) block.getBlockData();
                if (!data.getHalf().equals(Bisected.Half.TOP)) return;
                Door door = (Door) bottomBlock.getBlockData();
                door.setOpen(data.isOpen());
                bottomBlock.setBlockData(door);
                bottomBlock.getState().update(true, true);
            }
            event.setCancelled(true);
        }
        if (block.getType() == Material.NOTE_BLOCK) event.setCancelled(true);
        if (!Tag.SIGNS.isTagged(block.getType()) && !block.getType().equals(Material.LECTERN))
            block.getState().update(true, true);
    }

    //NoteBlock クラフト出来なくする
    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if (event.getRecipe().getResult().getType() == Material.NOTE_BLOCK)
            event.setCancelled(true);
    }

    //NoteBlock をピストンから保護
    @EventHandler
    public void onPistonExtends(BlockPistonExtendEvent event) {
        if (event.getBlocks().stream().anyMatch(block -> block.getType().equals(Material.NOTE_BLOCK)))
            event.setCancelled(true);
    }

    //NoteBlock を粘着ピストンから保護
    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        if (event.getBlocks().stream().anyMatch(block -> block.getType().equals(Material.NOTE_BLOCK)))
            event.setCancelled(true);
    }

    //NoteBlock の音階を変化させない
    @EventHandler
    public void onNotePlay(NotePlayEvent event) {
        event.setCancelled(true);
    }


}

