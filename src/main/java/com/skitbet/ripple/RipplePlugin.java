package com.skitbet.ripple;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class RipplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (label.equals("test")) {
            Player player = (Player) sender;

            Location location = player.getLocation().clone();
            location.setY(location.getY() - 1);

            Ripple rippleEffect = new Ripple(location, 1.0, 0.15);
            rippleEffect.runTaskTimer(this, 0L, 1L); // Adjust the delay and period as needed

        }

        return true;
    }

    public class Ripple extends BukkitRunnable {

        private final Location centerLoc;
        private double radius;
        private final double interval;


        public Ripple(Location centerLoc, double radius, double interval) {
            this.centerLoc = centerLoc;
            this.radius = radius;
            this.interval = interval;
        }


        @Override
        public void run() {
            World world = centerLoc.getWorld();
            double x = centerLoc.getX();
            double y = centerLoc.getY();
            double z = centerLoc.getZ();

            for (double theta = 0; theta <= 2 * Math.PI; theta += interval) {
                double dx = radius * Math.cos(theta);
                double dz = radius * Math.sin(theta);

                Location newLoc = new Location(world, x + dx, y, z + dz);

                FallingBlock fallingBlock = (FallingBlock) world.spawnEntity(newLoc, EntityType.FALLING_BLOCK);
                fallingBlock.setVelocity(new Vector(0, 0.2, 0));
            }

            radius += interval;
            if (radius > 10) { // Adjust the maximum radius as needed
                this.cancel(); // Stop the ripple effect
            }
        }
    }
}