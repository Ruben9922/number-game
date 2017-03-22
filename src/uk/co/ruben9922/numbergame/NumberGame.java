package uk.co.ruben9922.numbergame;

import org.jetbrains.annotations.NotNull;
import uk.co.ruben9922.utilities.consoleutilities.InputUtilities;

import java.util.*;

public class NumberGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        // Welcome message
        System.out.println("Number Game\n");

        List<Player> players = createPlayers(scanner);

        List<Tile> tiles = generateTiles(2, 1, 13, Arrays.asList(new Tile[] {
                new SmileyTile(Tile.Colour.BLACK),
                new SmileyTile(Tile.Colour.ORANGE)
        }));

        // Give players tiles
        final int INITIAL_PLAYER_TILE_COUNT = 14;
        System.out.format("Giving each player %d tiles...\n\n", INITIAL_PLAYER_TILE_COUNT);
        for (Player player : players) {
            givePlayerTiles(random, player, tiles, INITIAL_PLAYER_TILE_COUNT);
        }

        // Main game
        for (Player player : players) {
            System.out.format("%s's Turn\n", player.getName());
            System.out.println("Tiles: ");
            printTiles(player.getTiles());

            System.out.println();
        }
    }

    @NotNull
    private static List<Player> createPlayers(Scanner scanner) {
        // Input number of players
        final int PLAYER_COUNT = InputUtilities.inputInt(scanner, "Number of players: ", 0, null);
        scanner.nextLine();

        // Initialise array then, for each player, input name, create Player object and add object to array
        List<Player> players = new ArrayList<>(PLAYER_COUNT);
        for (int i = 0; i < PLAYER_COUNT; i++) {
            String playerName = inputPlayerName(scanner, players);
            players.add(new Player(playerName));
            System.out.format("Player \"%s\" added.\n\n", playerName);
        }

        return players;
    }

    private static String inputPlayerName(Scanner scanner, List<Player> existingPlayers) {
        String playerName;
        boolean unique;
        do {
            System.out.format("Player %1$d's name (leave blank for \"Player %1$d\"): ", existingPlayers.size() + 1);
            playerName = scanner.nextLine().trim();

            unique = true;
            if (playerName.isEmpty()) {
                // Implementing the "leave blank for ..." from prompt above
                playerName = "Player " + (existingPlayers.size() + 1);
            } else {
                // Check for uniqueness
                for (Player player : existingPlayers) {
                    if (playerName.toLowerCase().equals(player.getName().toLowerCase())) {
                        unique = false;
                        System.out.format("The name %s is already taken! Enter a different name.\n", player.getName()); // Might move this later
                        break;
                    }
                }
            }
        } while (!unique);
        return playerName;
    }

    // minTileNumber and maxTileNumber are both inclusive
    private static List<Tile> generateTiles(int numberTileCopies, int minTileNumber, int maxTileNumber, List<Tile> extraTiles) {
        Tile.Colour[] colourValues = Tile.Colour.values();
        List<Tile> tiles = new LinkedList<>();

        for (Tile.Colour colour : colourValues) {
            for (int i = 0; i < numberTileCopies; i++) {
                for (int j = minTileNumber; j <= maxTileNumber; j++) {
                    tiles.add(new NumberTile(colour, j));
                }
            }
        }

        for (Tile tile : extraTiles) {
            tiles.add(tile);
        }

        return tiles;
    }

    private static void givePlayerTiles(Random random, Player player, List<Tile> tiles, int tileCount) {
        for (int i = 0; i < tileCount; i++) {
            int index = random.nextInt(tiles.size());
            Tile tile = tiles.remove(index);
            player.getTiles().add(tile);
        }
    }

    private static void printTiles(List<Tile> tiles) {
        ListIterator<Tile> listIterator = tiles.listIterator();
        while (listIterator.hasNext()) {
            Tile tile = listIterator.next();
            int index = listIterator.nextIndex();
            System.out.format("[%d] %s\n", index, tile.toString());
        }
    }
}
