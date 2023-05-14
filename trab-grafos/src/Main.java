import org.jgrapht.generate.GnmRandomGraphGenerator;
import org.jgrapht.graph.*;
import java.util.*;
import java.util.function.Supplier;

public class Main {
    // Gera um grafo direcionado e com arestas de peso aleatórios

    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        int option = 0;
        int edgeNumber = 10;
        int vertexNumber = edgeNumber * 2;
        /*
        System.out.println("Qual o número de vertices do grafo que você deseja gerar?");
        System.out.println("1 - 100 vértices");
        System.out.println("2 - 1.000 vértices");
        System.out.println("3 - 10.000 vértices");
        System.out.println("4 - 100.000 vértices");
        */

        DefaultDirectedWeightedGraph<Integer, DefaultWeightedEdge> g = autoGrafo(vertexNumber,edgeNumber);

        Set<DefaultWeightedEdge> arestas = g.edgeSet();
        Set<Integer> vertices =  g.vertexSet();

        Queue<Integer> fila = new ArrayDeque<>();
        int t = 0;

        System.out.println(g.vertexSet());
        System.out.println(g.edgeSet());
    }

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

    public static List<Integer> buscaEmLargura(DefaultDirectedWeightedGraph<Integer, DefaultWeightedEdge> g, Integer v){
        Set<Integer> visitados = new HashSet<>();
        Queue<Integer> fila = new LinkedList<>();
        List<Integer> visitadosNaOrdem = new LinkedList<>();

        visitados.add(v);
        fila.add(v);
        visitadosNaOrdem.add(v);

        while (!fila.isEmpty()) {
            Integer verticeAtual = fila.poll();
            for (DefaultWeightedEdge aresta : g.outgoingEdgesOf(verticeAtual)) {
                Integer vizinho = g.getEdgeTarget(aresta);
                if (!visitados.contains(vizinho)) {
                    visitados.add(vizinho);
                    fila.add(vizinho);
                    visitadosNaOrdem.add(vizinho);
                }
            }
        }

        return visitadosNaOrdem;

        }







}
