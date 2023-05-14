import org.jgrapht.generate.GnmRandomGraphGenerator;
import org.jgrapht.graph.*;
import java.util.*;
import java.util.function.Supplier;

public class Main {
    // Gera um grafo direcionado e com arestas de peso aleatórios

    public static void main(String[] args){
        Scanner input = new Scanner(System.in);

        System.out.println("Qual o número de vertices do grafo que você deseja gerar?");
        System.out.println("100 vértices");
        System.out.println("1.000 vértices");
        System.out.println("10.000 vértices");
        System.out.println("100.000 vértices");

        int edgeNumber = input.nextInt();
        int vertexNumber = edgeNumber * 2;

        if(edgeNumber < 100 || edgeNumber > 100000){
            edgeNumber = 1000;
        }

        //temporizador iniciado
        long startTime = System.nanoTime();


        DefaultDirectedWeightedGraph<Integer, DefaultWeightedEdge> g = autoGrafo(vertexNumber,edgeNumber);

        System.out.println("Digite a opção baseada no que você deseja fazer: ");
        System.out.println("1 - Verificar dados do grafo");
        System.out.println("2 - Fecho transitivo direto");
        System.out.println("3 - Fecho transitivo indireto");
        System.out.println("");

        int opcao = input.nextInt();

        switch (opcao){
            case 1:
                System.out.println("Número vértices: " + g.vertexSet().size());
                System.out.println("Número vértices: " + g.edgeSet().size());
                break;
            case 2:
                for(int i = 1; i <= edgeNumber;i++){
                    List<Integer> fechoTransitivoDireto = buscaEmLargura(g,i);
                    if(fechoTransitivoDireto.isEmpty())
                        System.out.println("O vertice: [ " + i + " ] não possui fecho transitivo.");
                    else {
                        Integer[] caminhos = new Integer[fechoTransitivoDireto.size()];
                        fechoTransitivoDireto.toArray(caminhos);
                        System.out.println("Eixo transitivo do vertice: [ " + i + " ] :");
                        for(int j = 0; j != caminhos.length; j++){
                            System.out.println(caminhos[j]);
                        }
                        System.out.println("------------------------------------------------------------");
                    }
                }
                break;
            case 3:

            default:

        }
        // termino cronometro
        long endTime = System.nanoTime();
        // diferença entre os valores de tempo
        long timeElapsed = endTime - startTime;
        float miliseconds = timeElapsed / 1000000;

        System.out.println("Execution time in nanoseconds: " + timeElapsed);
        System.out.println("Execution time in milliseconds: " + miliseconds);
        System.out.println("Execution time in seconds: " + miliseconds / 1000);
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

    public static List<Integer> verBase(DefaultDirectedWeightedGraph<Integer, DefaultWeightedEdge> g){
        List<Integer> base = new LinkedList<>();




        return base;
    }



}
