package island.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class GeneticAlgorithm {

    private List<String> population;
    private int populationSize;
    private int stringLength;
    private int bestFitness;
    private Random random;
    private String target;
    private double mutationRate;

    public GeneticAlgorithm() {
        this.populationSize = GAUtils.POPULATION_SIZE;
        this.stringLength = GAUtils.TARGET.length();
        this.random = new Random();
        this.target = GAUtils.TARGET;
        this.mutationRate = GAUtils.MUTATION_RATE;
        initializePopulation();
    }

    private void initializePopulation() {
        population = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            population.add(generateRandomString());
        }
    }

    private String generateRandomString() {
        StringBuilder sb = new StringBuilder(stringLength);
        for (int i = 0; i < stringLength; i++) {
            char c = newChar();
            sb.append(c);
        }
        return sb.toString();
    }

    public void run() {
        List<String> newPopulation = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            String parent1 = getBestIndividual();
            String parent2 = getBestIndividual();
            String child = crossover(parent1, parent2);
            child = mutate(child);
            newPopulation.add(child);
        }
        population = newPopulation;
    }

    private char newChar() {
        random = new Random();
        int c = (int) Math.floor(Math.random() * (122 - 63 + 1)) + 63;
        if (c == 63) {
            c = 32;
        }
        if (c == 64) {
            c = 46;
        }
        return (char) c;
    }


    public String getBestIndividual() {
        // Tournament selection
        int tournamentSize = 5;
        String best = null;
        bestFitness = Integer.MAX_VALUE;

        for (int i = 0; i < tournamentSize; i++) {
            String candidate = population.get(random.nextInt(populationSize));
            int candidateFitness = fitness(candidate);
            if (candidateFitness < bestFitness) {
                best = candidate;
                bestFitness = candidateFitness;
            }
        }
        return best;
    }

    public int fitness(String candidate) {
        int score = 0;
        for (int i = 0; i < stringLength; i++) {
            if (candidate.charAt(i) != target.charAt(i)) {
                score++;
            }
        }
        return score;
    }

    public String crossover(String parent1, String parent2) {
        StringBuilder child = new StringBuilder(stringLength);
        for (int i = 0; i < stringLength; i++) {
            char gene = random.nextBoolean() ? parent1.charAt(i) : parent2.charAt(i);
            child.append(gene);
        }
        return child.toString();
    }

    public String mutate(String individual) {
        StringBuilder mutated = new StringBuilder(stringLength);
        for (int i = 0; i < stringLength; i++) {
            char gene = individual.charAt(i);
            if (random.nextDouble() < mutationRate) {
                gene = newChar();
            }
            mutated.append(gene);
        }
        return mutated.toString();
    }

    public List<String> getPopulation() {
        return population;
    }
}
