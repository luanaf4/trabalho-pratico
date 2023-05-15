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
        System.out.println("2 - Fecho transitivo direto por naive");
        System.out.println("3 - Fecho transitivo inverso por naive");
        System.out.println("4 - Base e antibase pelo método de Warshall");
        System.out.println("5 - Base e antibase pelo método de naive");
        System.out.println("6 - Fecho transitivo direto pelo método de Warshall");
        System.out.println("7 - Fecho transitivo inverso pelo método de Warshall");
        System.out.println("");

        int opcao = input.nextInt();

        switch (opcao){
            case 1:
                System.out.println("Número arestas: " + g.vertexSet().size());
                System.out.println("Número vértices: " + g.edgeSet().size());
                break;
            case 2:
                fechoTransitivo(g);
                break;
            case 3:
                //implementar fecho transitivo inverso
                break;
            case 4:
                warshall(g);
                break;
            case 5:
                naive(g);
                break;
            case 6:
                fechoTransitivoDiretoWarshall(g);
                break;
            default:
                System.out.println("Opção inválida");
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

    public static void fechoTransitivo(DefaultDirectedWeightedGraph<Integer, DefaultWeightedEdge> g){
        int numVertice = g.edgeSet().size();
        int numArestas = g.vertexSet().size();
        for(int i = 1; i <= numVertice;i++){
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

    public static void warshall(DefaultDirectedWeightedGraph<Integer, DefaultWeightedEdge> g) {
        int n = g.vertexSet().size();
        boolean[][] alcancabilidade = new boolean[n][n];

        //Inicializa a matriz de alcançabilidade com base nas arestas existentes no grafo
        for (DefaultWeightedEdge edge : g.edgeSet()) {
            int origem = g.getEdgeSource(edge) - 1;
            int destino = g.getEdgeTarget(edge) - 1;
            alcancabilidade[origem][destino] = true;
        }

        //Algoritmo de Warshall
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    alcancabilidade[i][j] = alcancabilidade[i][j] || (alcancabilidade[i][k] && alcancabilidade[k][j]);
                }
            }
        }

        //Listas para definir base e antibase
        List<Integer> base = new ArrayList<>();
        List<Integer> antibase = new ArrayList<>();

        //Vértices que pertencem à base e antibase
        for (int i = 0; i < n; i++) {
            boolean inBase = true;
            for (int j = 0; j < n; j++) {
                if (i != j && alcancabilidade[i][j]) {
                    inBase = false;
                    break;
                }
            }
            if (inBase) {
                base.add(i + 1);
            } else {
                antibase.add(i + 1);
            }
        }

        //Imprime a base e antibase
        System.out.println("Base:");
        for (Integer vertex : base) {
            System.out.println(vertex);
        }
        System.out.println("Antibase:");
        for (Integer vertex : antibase) {
            System.out.println(vertex);
        }
    }


    //método que realiza uma busca em largura para saber se um vértice é alcançavel por meio de outro vértice
    private static boolean isReachable(DefaultDirectedWeightedGraph<Integer, DefaultWeightedEdge> g, int source, int target) {
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();

        visited.add(source);
        queue.add(source);

        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            if (vertex == target) {
                return true;
            }
            for (DefaultWeightedEdge edge : g.outgoingEdgesOf(vertex)) {
                int neighbor = g.getEdgeTarget(edge);
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        return false;
    }


    // Base e antibase pelo método naive utilizando o método isReachable como auxílio
    public static void naive(DefaultDirectedWeightedGraph<Integer, DefaultWeightedEdge> g) {
        int n = g.vertexSet().size();
        List<Integer> base = new ArrayList<>();
        List<Integer> antibase = new ArrayList<>();

        for (int vertex = 1; vertex <= n; vertex++) {
            boolean isInBase = true;
            for (int otherVertex = 1; otherVertex <= n; otherVertex++) {
                if (vertex != otherVertex) {
                    if (isReachable(g, otherVertex, vertex)) {
                        isInBase = false;
                        break;
                    }
                }
            }
            if (isInBase) {
                base.add(vertex);
            } else {
                antibase.add(vertex);
            }
        }

        // Imprime a base e antibase no console
        System.out.println("Base:");
        for (Integer vertex : base) {
            System.out.println(vertex);
        }
        System.out.println("Antibase:");
        for (Integer vertex : antibase) {
            System.out.println(vertex);
        }
    }

    public static void fechoTransitivoDiretoWarshall(DefaultDirectedWeightedGraph<Integer, DefaultWeightedEdge> g) {
        int n = g.vertexSet().size();
        boolean[][] alcancabilidade = new boolean[n][n];

        // Inicializa a matriz de alcançabilidade com base nas arestas existentes no grafo
        for (DefaultWeightedEdge edge : g.edgeSet()) {
            int origem = g.getEdgeSource(edge) - 1;
            int destino = g.getEdgeTarget(edge) - 1;
            alcancabilidade[origem][destino] = true;
        }

        // Algoritmo de Warshall
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    alcancabilidade[i][j] = alcancabilidade[i][j] || (alcancabilidade[i][k] && alcancabilidade[k][j]);
                }
            }
        }

        // Imprime o fecho transitivo direto de cada vértice
        for (int i = 0; i < n; i++) {
            System.out.println("Fecho transitivo direto do vértice " + (i + 1) + ":");
            for (int j = 0; j < n; j++) {
                if (alcancabilidade[i][j]) {
                    System.out.print((j + 1) + " ");
                }
            }
            System.out.println();
        }
    }




}
