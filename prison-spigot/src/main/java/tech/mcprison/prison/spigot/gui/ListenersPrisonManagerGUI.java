package tech.mcprison.prison.spigot.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitScheduler;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.mine.*;
import tech.mcprison.prison.spigot.gui.rank.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;


/**
 * @author GABRYCA
 */
public class ListenersPrisonManagerGUI implements Listener {

    SpigotPrison plugin;
    public List <String> activeGui = new ArrayList<String>();
    public boolean isChatEventActive = false;
    public int id;
    public String rankNameOfChat;

    public ListenersPrisonManagerGUI(){}
    public ListenersPrisonManagerGUI(SpigotPrison instance){
        plugin = instance;
    }

    @EventHandler
    public void onGuiClosing(InventoryCloseEvent e){

        Player p = (Player) e.getPlayer();

        if(activeGui.contains(p.getName()))
            activeGui.remove(p.getName());
    }


    public void addToGUIBlocker(Player p){
        if(!activeGui.contains(p.getName()))
            activeGui.add(p.getName());
    }

    public void removeFromGUIBlocker(Player p){
        if(activeGui.contains(p.getName()))
            activeGui.remove(p.getName());
    }

    public boolean invalidClick(Player player, InventoryClickEvent event){
        if(activeGui.contains(player.getName()))
            if(event.getSlot() == -999 // Checks if player clicks outside the inventory
                    || event.getCurrentItem() == null // Checks for invalid item
                    || event.getCurrentItem().getType() == Material.AIR) // Checks for clicking empty slot
                return true;
        return false;
    }

    @EventHandler
    public void onOpenInventory(InventoryOpenEvent e){
        Player p = (Player) e.getPlayer();

        if (e.getView().getTitle().substring(2).equalsIgnoreCase("PrisonManager") ||
                e.getView().getTitle().substring(2).equalsIgnoreCase("RanksManager -> Ladders") ||
                e.getView().getTitle().substring(2).equalsIgnoreCase("Ladders -> Ranks") ||
                e.getView().getTitle().substring(2).equalsIgnoreCase("Ranks -> RankUPCommands") ||
                e.getView().getTitle().substring(2).equalsIgnoreCase("MinesManager -> Mines") ||
                e.getView().getTitle().substring(2).equalsIgnoreCase("Mines -> MineInfo") ||
                e.getView().getTitle().substring(2).equalsIgnoreCase("Mines -> Delete") ||
                e.getView().getTitle().substring(2).equalsIgnoreCase("MineInfo -> Blocks")||
                e.getView().getTitle().substring(2).equalsIgnoreCase("MineInfo -> MineNotifications") ||
                e.getView().getTitle().substring(2).equalsIgnoreCase("Ranks -> RankManager")){

            // Add the player to the list of those who can't move items in the inventory
            addToGUIBlocker(p);

        }
    }

    @EventHandler
    public void onDragEvent(InventoryDragEvent e){
        Player p = (Player) e.getWhoClicked();

        // If you click an empty slot, this should avoid the error
            if(activeGui.contains(p.getName())) {
                e.setCancelled(true);
            }
    }

    @EventHandler
    public void onMoveItem(InventoryMoveItemEvent e){
        Player p = (Player) e.getHandlers();

        // If you click an empty slot, this should avoid the error
            if(activeGui.contains(p.getName())) {
                e.setCancelled(true);
            }
    }

    @EventHandler
    public void onInteractEvent(InventoryInteractEvent e){

        Player p = (Player) e.getWhoClicked();

            if(activeGui.contains(p.getName())) {
                e.setCancelled(true);
            }
    }

    @EventHandler
    public void onPickupItem(InventoryPickupItemEvent e){
        Player p = (Player) e.getHandlers();

        if(activeGui.contains(p.getName())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onCreativeEvent(InventoryCreativeEvent e){

        Player p = (Player) e.getWhoClicked();

        // If you click an empty slot, this should avoid the error
        if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
            if(activeGui.contains(p.getName())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e)
    {
        if (isChatEventActive){
            Player p = e.getPlayer();
            String message = e.getMessage();
            Bukkit.getScheduler().cancelTask(id);
            if (message.equalsIgnoreCase("close")){
                isChatEventActive = false;
                p.sendMessage(SpigotPrison.format("&cRename tag closed, nothing got changed"));
            } else {
                Bukkit.getScheduler().runTask(SpigotPrison.getInstance(), () -> {
                    Bukkit.getServer().dispatchCommand(p, "ranks set tag " + rankNameOfChat + " " + message);
                });
                isChatEventActive = false;
            }
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onClick(InventoryClickEvent e){

        Player p = (Player) e.getWhoClicked();

        // If you click an empty slot, this should avoid the error
        if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
            if(activeGui.contains(p.getName())) {
                e.setCancelled(true);
            }
            return;
        }

        // check if the item has itemMeta
        if (!(e.getCurrentItem().hasItemMeta())){
            return;
        }

        // Check if the GUI have the right title and do the actions
        switch (e.getView().getTitle().substring(2)) {
            case "PrisonManager":

                // Check the Item display name and do open the right GUI
                if (e.getCurrentItem().getItemMeta().getDisplayName().substring(2).equals("Ranks")) {
                    SpigotLaddersGUI gui = new SpigotLaddersGUI(p);
                    gui.open();
                }

                // Check the Item display name and do open the right GUI
                else if (e.getCurrentItem().getItemMeta().getDisplayName().substring(2).equals("Prison Tasks")) {
                    e.setCancelled(true);
                    return;
                }

                // Check the Item display name and do open the right GUI
                else if (e.getCurrentItem().getItemMeta().getDisplayName().substring(2).equals("Mines")) {
                    SpigotMinesGUI gui = new SpigotMinesGUI(p);
                    gui.open();
                }

                e.setCancelled(true);

                // Check if the GUI have the right title and do the actions
                break;
            case "RanksManager -> Ladders": {

                // Get the ladder name or the button name
                String ladderName = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);

                // Get the ladder by the name of the button got before
                Optional<RankLadder> ladder = PrisonRanks.getInstance().getLadderManager().getLadder(ladderName);

                // Check if the ladder exist, everything can happen but this shouldn't
                if (!ladder.isPresent()) {
                    p.sendMessage("What did you actually click? Sorry ladder not found.");
                    return;
                }

                // When the player click an item with shift and right click, e.isShiftClick should be enough but i want
                // to be sure's a right click
                if (e.isShiftClick() && e.isRightClick()) {

                    // Execute the command
                    Bukkit.dispatchCommand(p, "ranks ladder delete " + ladderName);
                    p.closeInventory();
                    SpigotLaddersGUI gui = new SpigotLaddersGUI(p);
                    gui.open();
                    return;

                }

                // Open the GUI of ranks
                SpigotRanksGUI gui = new SpigotRanksGUI(p, ladder);
                gui.open();

                e.setCancelled(true);
                break;
            }
            // Check the title of the inventory and do the actions
            case "Ladders -> Ranks": {

                String rankName = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);

                // Get the rank
                Optional<Rank> rankOptional = PrisonRanks.getInstance().getRankManager().getRank(rankName);

                // Check if the rank exist
                if (!rankOptional.isPresent()) {
                    p.sendMessage(SpigotPrison.format("&cThe rank " + rankName + " does not exist."));
                    return;
                }

                Rank rank = rankOptional.get();

                if (e.isShiftClick() && e.isRightClick()) {

                    Bukkit.dispatchCommand(p, "ranks delete " + rankName);
                    p.closeInventory();

                } else {

                    SpigotRankManagerGUI gui = new SpigotRankManagerGUI(p, rank);
                    gui.open();

                }

                e.setCancelled(true);
                break;
            }
            // Check the title of the inventory and do things
            case "Ranks -> RankManager": {

                String buttonnamemain = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);

                // Split the button at the space between the buttonname and the rankname
                String[] parts = buttonnamemain.split(" ");

                // Output finally the buttonname and the minename explicit out of the array
                String buttonname = parts[0];
                String rankName = parts[1];

                // Get the rank
                Optional<Rank> rankOptional = PrisonRanks.getInstance().getRankManager().getRank(rankName);

                if (buttonname.equalsIgnoreCase("RankupCommands")){

                    // Check if the rank exist
                    if (!rankOptional.isPresent()) {
                        p.sendMessage(SpigotPrison.format("&cThe rank " + rankName + " does not exist."));
                        return;
                    }

                    Rank rank = rankOptional.get();

                    if (rank.rankUpCommands == null) {

                        p.sendMessage(SpigotPrison.format("&cThere aren't commands for this rank anymore."));

                    }

                    // Open the GUI of commands
                    else {

                        SpigotRankUPCommandsGUI gui = new SpigotRankUPCommandsGUI(p, rank);
                        gui.open();

                    }

                } else if (buttonname.equalsIgnoreCase("RankPrice")){

                    SpigotRankPriceGUI gui = new SpigotRankPriceGUI(p, (int) rankOptional.get().cost, rankOptional.get().name);
                    gui.open();

                } else if (buttonname.equalsIgnoreCase("RankTag")){

                    p.sendMessage(SpigotPrison.format("&3Please write the &6tag &3you'd like to use and &6submit&3."));
                    p.sendMessage(SpigotPrison.format("&3Input &cclose &3to cancel or wait &c30 seconds&3."));
                    isChatEventActive = true;
                    rankNameOfChat = rankName;
                    id = Bukkit.getScheduler().scheduleSyncDelayedTask(SpigotPrison.getInstance(), () -> {
                        isChatEventActive = false;
                        p.sendMessage(SpigotPrison.format("&cYou run out of time, tag not changed."));
                    }, 20L * 30);
                    p.closeInventory();
                }

                e.setCancelled(true);
                break;
            }
            case "RankManager -> RankUPCommands": {

                String command = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);

                if (e.isShiftClick() && e.isRightClick()) {

                    Bukkit.dispatchCommand(p, "ranks command remove " + command);
                    p.closeInventory();

                }

                e.setCancelled(true);

                // Check the title of the inventory and do the actions
                break;
            }
            // Check the inventory name and do the actions
            case "RankManager -> RankPrice": {

                // Get the button name
                String buttonnamemain = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);

                // Split the button name in parts
                String[] parts = buttonnamemain.split(" ");

                // Rename the parts
                String part1 = parts[0];
                String part2 = parts[1];
                String part3 = parts[2];

                // Initialize the variable
                int decreaseOrIncreaseValue = 0;

                // If there're enough parts init another variable
                if (parts.length == 4){
                    decreaseOrIncreaseValue = Integer.parseInt(parts[3]);
                }

                // Check the button name and do the actions
                if (part1.equalsIgnoreCase("Confirm:")) {

                    // Check the click type and do the actions
                    if (e.isLeftClick()){

                        // Execute the command
                        Bukkit.dispatchCommand(p,"ranks set cost " + part2 + " " + part3);

                        // Close the inventory
                        p.closeInventory();

                        return;

                        // Check the click type and do the actions
                    } else if (e.isRightClick()){

                        // Send a message to the player
                        p.sendMessage(SpigotPrison.format("&cEvent cancelled."));

                        // Close the inventory
                        p.closeInventory();

                        return;
                    } else {

                        // Cancel the event
                        e.setCancelled(true);
                        return;
                    }
                }

                // Give to val a value
                int val = Integer.parseInt(part2);

                // Check the calculator symbol
                if (part3.equals("-")){

                    // Check if the value's already too low
                    if (!((val -  decreaseOrIncreaseValue) < 0)) {

                        // If it isn't too low then decrease it
                        val = val - decreaseOrIncreaseValue;

                        // If it is too low
                    } else {

                        // Tell to the player that the value's too low
                        p.sendMessage(SpigotPrison.format("&cToo low value."));

                        // Close the inventory
                        p.closeInventory();
                        return;
                    }

                    // Open an updated GUI after the value changed
                    SpigotRankPriceGUI gui = new SpigotRankPriceGUI(p, val, part1);
                    gui.open();

                    // Check the calculator symbol
                } else if (part3.equals("+")){

                    // Check if the value isn't too high
                    if (!((val + decreaseOrIncreaseValue) > 999999)) {

                        // Increase the value
                        val = val + decreaseOrIncreaseValue;

                        // If the value's too high then do the action
                    } else {

                        // Close the GUI and tell it to the player
                        p.sendMessage(SpigotPrison.format("&cToo high value."));
                        p.closeInventory();
                        return;
                    }

                    // Open a new updated GUI with new values
                    SpigotRankPriceGUI gui = new SpigotRankPriceGUI(p, val, part1);
                    gui.open();

                }

                break;
            }
            case "MinesManager -> Mines": {

                // Mine name or title of the item
                String minename = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);

                // Variables
                @SuppressWarnings("unused")
                Optional<Mine> mine = PrisonMines.getInstance().getMineManager().getMine(minename);
                PrisonMines pMines = PrisonMines.getInstance();
                Mine m = pMines.getMineManager().getMine(minename).get();

                // Check the clicks
                if (e.isShiftClick() && e.isRightClick()) {

                    Bukkit.dispatchCommand(p, "mines delete " + minename);
                    p.closeInventory();
                    SpigotMinesConfirmGUI gui = new SpigotMinesConfirmGUI(p, minename);
                    gui.open();
                    return;

                }

                // Open the GUI of mines info
                SpigotMineInfoGUI gui = new SpigotMineInfoGUI(p, m, minename);
                gui.open();

                e.setCancelled(true);

                // Check the title of the inventory and do the actions
                break;
            }
            case "Mines -> MineInfo": {

                // Get the button name without colors but with the minename too
                String buttonnamemain = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);

                // Split the button at the space between the buttonname and the minename
                String[] parts = buttonnamemain.split(" ");

                // Output finally the buttonname and the minename explicit out of the array
                String buttonname = parts[0];
                String mineName = parts[1];

                // Check the name of the button and do the actions
                switch (buttonname) {
                    case "Blocks_of_the_Mine:":

                        // Open the GUI
                        SpigotMinesBlocksGUI gui = new SpigotMinesBlocksGUI(p, mineName);
                        gui.open();

                        break;

                    // Check the name of the button and do the actions
                    case "Reset_Mine:":

                        // Execute the command
                        Bukkit.dispatchCommand(p, "mines reset " + mineName);

                        e.setCancelled(true);

                        break;

                    // Check the name of the button and do the actions
                    case "Mine_Spawn:":

                        // Execute the command
                        Bukkit.dispatchCommand(p, "mines set spawn " + mineName);

                        e.setCancelled(true);
                        break;

                    // Check the name of the button and do the actions
                    case "Mine_notifications:":

                        // Open the GUI
                        SpigotMineNotificationsGUI gui1 = new SpigotMineNotificationsGUI(p, mineName);
                        gui1.open();

                        break;

                    // Check the name of the button and do the actions
                    case "TP_to_the_Mine:":

                        // Close the inventory
                        p.closeInventory();

                        // Execute the Command
                        Bukkit.dispatchCommand(p, "mines tp " + mineName);

                        break;

                    // Check the name of the button and do the actions
                    case "Reset_Time:":

                        // Initialize the variables
                        PrisonMines pMines = PrisonMines.getInstance();
                        Mine m = pMines.getMineManager().getMine(mineName).get();
                        int val = m.getResetTime();

                        // Open the GUI
                        SpigotMineResetTimeGUI gui2 = new SpigotMineResetTimeGUI(p, val, mineName);
                        gui2.open();

                        break;
                }

                break;
            }

            // Check the title of the inventory and do the actions
            case "Mines -> Delete": {

                // Get the button name without colors but with the minename too
                String buttonnamemain = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);

                // Split the button at the space between the buttonname and the minename
                String[] parts = buttonnamemain.split(" ");

                // Output finally the buttonname and the minename explicit out of the array
                String buttonname = parts[0];
                String mineName = parts[1];

                // Check the name of the button and do the actions
                if (buttonname.equals("Confirm:")) {

                    // Confirm
                    Bukkit.dispatchCommand(p, "mines delete " + mineName + " confirm");

                    // Close the Inventory
                    p.closeInventory();

                // Check the name of the button and do the actions
                } else if (buttonname.equals("Cancel:")) {

                    // Cancel
                    Bukkit.dispatchCommand(p, "mines delete " + mineName + " cancel");

                    // Close the inventory
                    p.closeInventory();

                }

                break;
            }

            // Check the title of the inventory and do the actions
            case "MineInfo -> Blocks": {

                // Get the button name without colors but with the minename too
                String buttonnamemain = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);

                // Split the button at the space between the buttonname and the minename
                String[] parts = buttonnamemain.split(" ");

                // Output finally the buttonname and the minename explicit out of the array
                String buttonname = parts[0];
                String mineName = parts[1];

                // Check the click Type and do the actions
                if (e.isShiftClick() && e.isRightClick()) {

                    // Execute the command
                    Bukkit.dispatchCommand(p, "mines block remove " + mineName + " " + buttonname.substring(0, buttonname.length() - 1));

                    // Close the GUI so it can be updated
                    p.closeInventory();

                    // Open the GUI
                    SpigotMinesBlocksGUI gui = new SpigotMinesBlocksGUI(p, mineName);
                    gui.open();
                }

                // Cancel the event
                e.setCancelled(true);

                break;
            }

            // Check the inventory name and do the actions
            case "MinesInfo -> ResetTime": {

                // Get the button name
                String buttonnamemain = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);

                // Split the button name in parts
                String[] parts = buttonnamemain.split(" ");

                // Rename the parts
                String part1 = parts[0];
                String part2 = parts[1];
                String part3 = parts[2];

                // Initialize the variable
                int decreaseOrIncreaseValue = 0;

                // If there're enough parts init another variable
                if (parts.length == 4){
                    decreaseOrIncreaseValue = Integer.parseInt(parts[3]);
                }

                // Check the button name and do the actions
                if (part1.equalsIgnoreCase("Confirm:")) {

                    // Check the click type and do the actions
                    if (e.isLeftClick()){

                        // Execute the command
                        Bukkit.dispatchCommand(p,"mines resettime " + part2 + " " + part3);

                        // Close the inventory
                        p.closeInventory();

                        return;

                    // Check the click type and do the actions
                    } else if (e.isRightClick()){

                        // Send a message to the player
                        p.sendMessage(SpigotPrison.format("&cEvent cancelled."));

                        // Close the inventory
                        p.closeInventory();

                        return;
                    } else {

                        // Cancel the event
                        e.setCancelled(true);
                        return;
                    }
                }

                // Give to val a value
                int val = Integer.parseInt(part2);

                // Check the calculator symbol
                if (part3.equals("-")){

                    // Check if the value's already too low
                    if (!((val -  decreaseOrIncreaseValue) < 0)) {

                        // If it isn't too low then decrease it
                        val = val - decreaseOrIncreaseValue;

                    // If it is too low
                    } else {

                        // Tell to the player that the value's too low
                        p.sendMessage(SpigotPrison.format("&cToo low value."));

                        // Close the inventory
                        p.closeInventory();
                        return;
                    }

                    // Open an updated GUI after the value changed
                    SpigotMineResetTimeGUI gui = new SpigotMineResetTimeGUI(p, val, part1);
                    gui.open();

                // Check the calculator symbol
                } else if (part3.equals("+")){

                    // Check if the value isn't too high
                    if (!((val + decreaseOrIncreaseValue) > 999999)) {

                        // Increase the value
                        val = val + decreaseOrIncreaseValue;

                    // If the value's too high then do the action
                    } else {

                        // Close the GUI and tell it to the player
                        p.sendMessage(SpigotPrison.format("&cToo high value."));
                        p.closeInventory();
                        return;
                    }

                    // Open a new updated GUI with new values
                    SpigotMineResetTimeGUI gui = new SpigotMineResetTimeGUI(p, val, part1);
                    gui.open();

                }

                break;
            }

            // Check the inventory title and do the actions
            case "MineInfo -> MineNotifications": {

                // Get the button name without colors but with the minename too
                String buttonnamemain = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);

                // Split the button at the space between the buttonname and the minename
                String[] parts = buttonnamemain.split(" ");

                // Output finally the buttonname and the minename explicit out of the array
                String buttonname = parts[0];
                String mineName = parts[1];
                String typeNotification;
                long val;

                // Init variables
                PrisonMines pMines = PrisonMines.getInstance();
                Mine m = pMines.getMineManager().getMine(mineName).get();

                // Check the button name and do the actions
                if (buttonname.equalsIgnoreCase("Within_Mode:")){

                    // Change the value of the variable
                    typeNotification = "within";

                    // Execute command
                    Bukkit.dispatchCommand(p, "mines notification " + mineName + " " + typeNotification + " " + "0");

                    // Cancel the event and close the inventory
                    e.setCancelled(true);
                    p.closeInventory();

                    // Check the button name and do the actions
                } else if (buttonname.equalsIgnoreCase("Radius_Mode:")){

                    // Change the value of the variable
                    typeNotification = "radius";

                    // Get the variable value
                    val = m.getNotificationRadius();

                    // Open the GUI
                    SpigotMineNotificationRadiusGUI gui = new SpigotMineNotificationRadiusGUI(p, val,  typeNotification, mineName);
                    gui.open();

                // Check the button name and do the actions
                } else if (buttonname.equalsIgnoreCase("Disabled_Mode:")){

                    // Change the value of the variable
                    typeNotification = "disabled";

                    // Execute the command
                    Bukkit.dispatchCommand(p, "mines notification " + mineName + " " + typeNotification + " " + "0");

                    // Cancel the event and close the inventory
                    e.setCancelled(true);
                    p.closeInventory();

                }

                break;
            }

            // Check the inventory title and do the actions
            case "MineNotifications -> Radius": {

                // Get the button name
                String buttonnamemain = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);

                // Split the button name to parts
                String[] parts = buttonnamemain.split(" ");

                // Rename the variables
                String part1 = parts[0];
                String part2 = parts[1];
                String part3 = parts[2];
                String typeNotification;

                // Init the variable
                int decreaseOrIncreaseValue = 0;

                // Check the button name and do the actions
                if (!(part1.equalsIgnoreCase("Confirm:"))){

                    // Give them a value
                    decreaseOrIncreaseValue = Integer.parseInt(parts[3]);
                    typeNotification = parts[4];

                // Do others actions
                } else {

                    // Give it a value
                    typeNotification = parts[3];
                }

                // Check the button name and do the actions
                if (part1.equalsIgnoreCase("Confirm:")) {

                    // Check the click type
                    if (e.isLeftClick()){

                        // Execute the command
                        Bukkit.dispatchCommand(p,"mines notification " + part2 + " " + typeNotification + " " + part3);

                        // Close the inventory
                        p.closeInventory();

                        return;
                    } else if (e.isRightClick()){

                        // Close the inventory
                        p.sendMessage(SpigotPrison.format("&cEvent cancelled."));
                        p.closeInventory();

                        return;
                    } else {

                        // Cancel the event
                        e.setCancelled(true);
                        return;
                    }
                }

                // Init a new value
                long val = Integer.parseInt(part2);

                // Check the calculator symbol
                if (part3.equals("-")){

                    // Check if the value's too low
                    if (!((val -  decreaseOrIncreaseValue) < 0)) {

                        // Decrease the value
                        val = val - decreaseOrIncreaseValue;

                    // If the value's too low
                    } else {

                        // Close the inventory and tell it the player
                        p.sendMessage(SpigotPrison.format("&cToo low value."));
                        p.closeInventory();
                        return;
                    }

                    // Open a new updated GUI with new values
                    SpigotMineNotificationRadiusGUI gui = new SpigotMineNotificationRadiusGUI(p, val,  typeNotification, part1);
                    gui.open();

                // Check the calculator symbol
                } else if (part3.equals("+")){

                    // Check if the value's too high
                    if (!((val + decreaseOrIncreaseValue) > 9999999)) {

                        // Increase the value
                        val = val + decreaseOrIncreaseValue;

                    // If the value's too high
                    } else {

                        // Close the inventory and tell it to the player
                        p.sendMessage(SpigotPrison.format("&cToo high value."));
                        p.closeInventory();
                        return;
                    }

                    // Open a new updated GUI with new values
                    SpigotMineNotificationRadiusGUI gui = new SpigotMineNotificationRadiusGUI(p, val,  typeNotification, part1);
                    gui.open();

                }

                break;
            }

        }

        // Deleted the e.setCancelled(true) because'd make every chest impossible to use, sorry.

    }
}
