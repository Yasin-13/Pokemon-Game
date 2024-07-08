import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

abstract class Pokemon {
    protected String name;
    protected String type;
    protected int health, maxHealth, attack, defense, specialAttack, specialDefense, speed, level, experience;
    protected List<Move> moves;
    protected boolean isWild;

    public Pokemon(String name, String type, int maxHealth, int attack, int defense, int specialAttack,
                   int specialDefense, int speed, int level, int experience, boolean isWild) {
        this.name = name;
        this.type = type;
        this.health = maxHealth;
        this.maxHealth = maxHealth;
        this.attack = attack;
        this.defense = defense;
        this.specialAttack = specialAttack;
        this.specialDefense = specialDefense;
        this.speed = speed;
        this.level = level;
        this.experience = experience;
        this.moves = new ArrayList<>();
        this.isWild = isWild;
    }

    public void attack(Pokemon opponent, Move move) {
        if (Math.random() * 100 <= move.accuracy) {
            int damage = (move.power * this.attack / opponent.defense) / 2;
            opponent.takeDamage(damage);
            System.out.println(this.name + " used " + move.name + " and dealt " + damage + " damage!");
        } else {
            System.out.println(this.name + " used " + move.name + " but missed!");
        }
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health <= 0) {
            this.health = 0;
            System.out.println(this.name + " fainted!");
        }
    }

    public void gainExperience(int exp) {
        this.experience += exp;
        if (this.experience >= level * 100) {
            levelUp();
        }
    }

    public void levelUp() {
        this.level++;
        this.maxHealth += 10;
        this.health = this.maxHealth; 
        this.attack += 2;
        this.defense += 2;
        this.specialAttack += 2;
        this.specialDefense += 2;
        this.speed += 2;
        System.out.println(this.name + " leveled up to level " + this.level + "!");
    }

    public void learnMove(Move move) {
        if (moves.size() < 4) {
            moves.add(move);
            System.out.println(this.name + " learned " + move.name + "!");
        } else {
            System.out.println(this.name + " cannot learn more than 4 moves.");
        }
    }

    public int getStat(String stat) {
        switch (stat.toLowerCase()) {
            case "health":
                return health;
            case "max health":
                return maxHealth;
            case "attack":
                return attack;
            case "defense":
                return defense;
            case "special attack":
                return specialAttack;
            case "special defense":
                return specialDefense;
            case "speed":
                return speed;
            case "level":
                return level;
            case "experience":
                return experience;
            default:
                return -1;
        }
    }

    public void displayStats() {
        System.out.println("Name: " + name);
        System.out.println("Type: " + type);
        System.out.println("Health: " + health + "/" + maxHealth);
        System.out.println("Attack: " + attack);
        System.out.println("Defense: " + defense);
        System.out.println("Special Attack: " + specialAttack);
        System.out.println("Special Defense: " + specialDefense);
        System.out.println("Speed: " + speed);
        System.out.println("Level: " + level);
        System.out.println("Experience: " + experience);
    }

    public void heal() {
        this.health = this.maxHealth;
        System.out.println(this.name + " has been fully healed!");
    }
}

class ElectricPokemon extends Pokemon {
    public ElectricPokemon(String name, int maxHealth, int attack, int defense, int specialAttack,
                           int specialDefense, int speed, int level, int experience, boolean isWild) {
        super(name, "Electric", maxHealth, attack, defense, specialAttack, specialDefense, speed, level, experience, isWild);
    }
}

class FirePokemon extends Pokemon {
    public FirePokemon(String name, int maxHealth, int attack, int defense, int specialAttack,
                       int specialDefense, int speed, int level, int experience, boolean isWild) {
        super(name, "Fire", maxHealth, attack, defense, specialAttack, specialDefense, speed, level, experience, isWild);
    }
}

class Move {
    String name;
    String type;
    int power;
    int accuracy;
    String category;
    String effect;

    public Move(String name, String type, int power, int accuracy, String category, String effect) {
        this.name = name;
        this.type = type;
        this.power = power;
        this.accuracy = accuracy;
        this.category = category;
        this.effect = effect;
    }

    public void applyEffect(Pokemon target) {
        if (this.effect != null) {
            System.out.println(this.name + " applied " + this.effect + " on " + target.name);
        }
    }
}

class Trainer {
    String name;
    List<Pokemon> team;
    List<Item> inventory;

    public Trainer(String name) {
        this.name = name;
        this.team = new ArrayList<>();
        this.inventory = new ArrayList<>();
    }

    public void catchPokemon(Pokemon wildPokemon, Pokeball pokeball) {
        if (pokeball.catchPokemon(wildPokemon)) {
            this.team.add(wildPokemon);
            System.out.println(this.name + " caught " + wildPokemon.name + "!");
        } else {
            System.out.println(wildPokemon.name + " escaped!");
        }
    }

    public void useItem(Item item, Pokemon target) {
        item.use(target);
        inventory.remove(item);
    }

    public void battle(Trainer opponent) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(this.name + " is battling " + opponent.name + "!");
        while (true) {
            Pokemon myPokemon = this.team.get(0);  // Assuming the first Pokemon in the team is used
            Pokemon opponentPokemon = opponent.team.get(0);  // Same for the opponent

            System.out.println("Choose a move:");
            for (int i = 0; i < myPokemon.moves.size(); i++) {
                System.out.println((i + 1) + ": " + myPokemon.moves.get(i).name);
            }

            int choice = scanner.nextInt();
            if (choice < 1 || choice > myPokemon.moves.size()) {
                System.out.println("Invalid move. Try again.");
                continue;
            }

            Move chosenMove = myPokemon.moves.get(choice - 1);
            myPokemon.attack(opponentPokemon, chosenMove);

            if (opponentPokemon.health == 0) {
                System.out.println(opponentPokemon.name + " fainted! You win!");
                myPokemon.gainExperience(opponentPokemon.level * 50);  // Gaining experience after defeating an opponent
                break;
            }

            // Opponent's turn (simplified, they just use the first move)
            Move opponentMove = opponentPokemon.moves.get(0);
            opponentPokemon.attack(myPokemon, opponentMove);

            if (myPokemon.health == 0) {
                System.out.println(myPokemon.name + " fainted! You lose!");
                break;
            }
        }
    }

    public void visitPokeCenter() {
        for (Pokemon pokemon : team) {
            pokemon.heal();
        }
        System.out.println("All your Pokémon have been healed!");
    }
}

class Item {
    String name;
    String type;
    String effect;

    public Item(String name, String type, String effect) {
        this.name = name;
        this.type = type;
        this.effect = effect;
    }

    public void use(Pokemon target) {
        // Simplified item usage
        System.out.println(this.name + " used " + this.effect + " on " + target.name);
    }
}

class Pokeball extends Item {
    double catchRate;

    public Pokeball(String name, double catchRate) {
        super(name, "Pokeball", "Catch Pokemon");
        this.catchRate = catchRate;
    }

    public boolean catchPokemon(Pokemon wildPokemon) {
        double chance = Math.random();
        return chance <= catchRate;
    }
}

public class PokemonGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Creating a wild Pokemon
        Pokemon pikachu = new ElectricPokemon("Pikachu", 35, 55, 40, 50, 50, 90, 5, 0, true);
        Move thunderbolt = new Move("Thunderbolt", "Electric", 90, 100, "Special", "Paralyze");
        pikachu.learnMove(thunderbolt);

        // Creating a trainer
        Trainer ash = new Trainer("Ash");
        Pokemon charmander = new FirePokemon("Charmander", 39, 52, 43, 60, 50, 65, 5, 0, false);
        Move ember = new Move("Ember", "Fire", 40, 100, "Special", "Burn");
        charmander.learnMove(ember);
        ash.team.add(charmander);

        // Creating an opponent trainer
        Trainer gary = new Trainer("Gary");
        gary.team.add(pikachu);

        // Creating a Pokeball
        Pokeball pokeball = new Pokeball("Basic Pokeball", 0.5);
        ash.inventory.add(pokeball);

        // Game loop
        while (true) {
            System.out.println("\r\n" + //
                                " ________  ________  ___  __    _______   _____ ______   ________  ________      \r\n" + //
                                "|\\   __  \\|\\   __  \\|\\  \\|\\  \\ |\\  ___ \\ |\\   _ \\  _   \\|\\   __  \\|\\   ___  \\    \r\n" + //
                                "\\ \\  \\|\\  \\ \\  \\|\\  \\ \\  \\/  /|\\ \\   __/|\\ \\  \\\\\\__\\ \\  \\ \\  \\|\\  \\ \\  \\\\ \\  \\   \r\n" + //
                                " \\ \\   ____\\ \\  \\\\\\  \\ \\   ___  \\ \\  \\_|/_\\ \\  \\\\|__| \\  \\ \\  \\\\\\  \\ \\  \\\\ \\  \\  \r\n" + //
                                "  \\ \\  \\___|\\ \\  \\\\\\  \\ \\  \\\\ \\  \\ \\  \\_|\\ \\ \\  \\    \\ \\  \\ \\  \\\\\\  \\ \\  \\\\ \\  \\ \r\n" + //
                                "   \\ \\__\\    \\ \\_______\\ \\__\\\\ \\__\\ \\_______\\ \\__\\    \\ \\__\\ \\_______\\ \\__\\\\ \\__\\\r\n" + //
                                "    \\|__|     \\|_______|\\|__| \\|__|\\|_______|\\|__|     \\|__|\\|_______|\\|__| \\|__|\r\n" + //
                                "                                                                                 \r\n" + //
                                "                                                                                 \r\n" + //
                                "                                                                                 \r\n" + //
                                "");
            System.out.println("1: Battle");
            System.out.println("2: Catch Pokémon");
            System.out.println("3: Use Item");
            System.out.println("4: View Pokémon Stats");
            System.out.println("5: Visit Poké Center");
            System.out.println("6: Exit");

            int choice = scanner.nextInt();
            if (choice == 1) {
                ash.battle(gary);
            } else if (choice == 2) {
                ash.catchPokemon(pikachu, pokeball);
            } else if (choice == 3) {
                ash.useItem(pokeball, charmander);
            } else if (choice == 4) {
                System.out.println("Choose a Pokémon to view stats:");
                for (int i = 0; i < ash.team.size(); i++) {
                    System.out.println((i + 1) + ": " + ash.team.get(i).name);
                }
                int pokemonChoice = scanner.nextInt();
                if (pokemonChoice < 1 || pokemonChoice > ash.team.size()) {
                    System.out.println("Invalid choice. Try again.");
                } else {
                    ash.team.get(pokemonChoice - 1).displayStats();
                }
            } else if (choice == 5) {
                ash.visitPokeCenter();
            } else if (choice == 6) {
                System.out.println("Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
