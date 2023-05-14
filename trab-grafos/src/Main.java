import org.jgrapht.generate.GnmRandomGraphGenerator;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Supplier;

public class Main {
    // Gera um grafo direcionado e com arestas de peso aleatórios
    public static DefaultDirectedWeightedGraph<Integer, DefaultWeightedEdge> autoGrafo(int qtdV, int qtdE) {
        // Fornecedor de vértices do grafo
        Supplier<Integer> fornecedorVertices = new Supplier<Integer>() {
            private int id = 0;

            @Override
            public Integer get() {
                return ++id;
            }
        };
        // Fornecedor de arestas do grafo
        Supplier<DefaultWeightedEdge> fornecedorArestas = new Supplier<DefaultWeightedEdge>() {
            @Override
            public DefaultWeightedEdge get() {
                return new DefaultWeightedEdge();
            }
        };
        // Instancia o grafo direcionado e com peso nas arestas
        DefaultDirectedWeightedGraph<Integer, DefaultWeightedEdge> grafo = new DefaultDirectedWeightedGraph<Integer, DefaultWeightedEdge>(fornecedorVertices, fornecedorArestas);
        // Cria o gerador do grafo com a quantidade de arestas e vértices escolhidos
        GnmRandomGraphGenerator<Integer, DefaultWeightedEdge> gerador = new GnmRandomGraphGenerator<>(qtdV, qtdE);
        // Gera o grafo aleatório
        gerador.generateGraph(grafo);
        // Insere pesos aleatórios
        Random rand = new Random();
        for(DefaultWeightedEdge E : grafo.edgeSet()) {
            grafo.setEdgeWeight(E, rand.nextInt(50) + 1);
        }

        return grafo;
    }
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        int option = 0;
        int edgeNumber = 150;
        int vertexNumber = edgeNumber * 2;
        /*
        System.out.println("Qual o número de vertices do grafo que você deseja gerar?");
        System.out.println("1 - 100 vértices");
        System.out.println("2 - 1.000 vértices");
        System.out.println("3 - 10.000 vértices");
        System.out.println("4 - 100.000 vértices");
        */

        DefaultDirectedWeightedGraph<Integer, DefaultWeightedEdge> g = autoGrafo(vertexNumber,edgeNumber);
        System.out.println(g.vertexSet().size());
        System.out.println(g.edgeSet().size());
    }
}
