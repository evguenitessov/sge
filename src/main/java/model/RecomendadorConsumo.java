package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.NoFeasibleSolutionException;
import org.apache.commons.math3.optim.linear.NonNegativeConstraint;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

public class RecomendadorConsumo {
    /**
     * Devuelve la función objetivo.
     * Es la función que estamos buscando optimizar, es la que tiene la forma:
     * x1 + x2 + ... + xn = MáxZ
     */
    public static LinearObjectiveFunction FuncionObjetivo(Cliente unCliente) {
        return new LinearObjectiveFunction(
            unCliente.getDispositivos().stream().mapToDouble(d -> 1).toArray(),
            0
        );
    }

    /**
     * Devuelve la restricción de uso mínimo de un dispositivo.
     */
    public static LinearConstraint RestriccionUsoMinimo(Dispositivo unDispositivo, double[] coeficientes) {
        return new LinearConstraint(
            coeficientes,
            Relationship.GEQ,
            unDispositivo.getTipoDispositivo().getUsoMinimo()
        );
    }

    /**
     * Devuelve la restricción de uso máximo de un dispositivo.
     */
    public static LinearConstraint RestriccionUsoMaximo(Dispositivo unDispositivo, double[] coeficientes) {
        return new LinearConstraint(
            coeficientes,
            Relationship.LEQ,
            unDispositivo.getTipoDispositivo().getUsoMaximo()
        );
    }

    /**
     * Devuelve la colección de restricciones de uso mensual de cada dispositivo de un cliente.
     */
    public static Collection<LinearConstraint> RestriccionesDeUsoMensual(Cliente unCliente) {
        Collection<LinearConstraint> restricciones = new ArrayList<>();
        double variables[] = unCliente.getDispositivos().stream().mapToDouble(d -> 0).toArray();
        int indice = 0;
        for(Dispositivo dispositivo : unCliente.getDispositivos()) {
            double variableActual[] = Arrays.copyOf(variables, variables.length);
            variableActual[indice] = 1;
            restricciones.add(RecomendadorConsumo.RestriccionUsoMinimo(dispositivo, variableActual));
            restricciones.add(RecomendadorConsumo.RestriccionUsoMaximo(dispositivo, variableActual));
            indice++;
        }

        return restricciones;
    }

    private double restriccionEnergetica;

    /**
     * Crea un recomendador de consumo
     * @param restriccionEnergetica El máximo de energía mensual permitida para esta recomendación.
     */
    public RecomendadorConsumo(double restriccionEnergetica) {
        this.restriccionEnergetica = restriccionEnergetica;
    }

    public double getRestriccionEnergetica() {
        return this.restriccionEnergetica;
    }

    /**
     * Devuelve un diccionario con los dispositivos del cliente y el consumo recomendado.
     * @throws ClienteNoRecomendableException
     */
    public Map<Dispositivo, Double> recomendarHoras(Cliente unCliente) throws ClienteNoRecomendableException {
        Map<Dispositivo, Double> recomendaciones = new HashMap<>();
        LinearObjectiveFunction funcionEconomica = RecomendadorConsumo.FuncionObjetivo(unCliente);
        Collection<LinearConstraint> restricciones = new ArrayList<>();
        restricciones.addAll(RecomendadorConsumo.RestriccionesDeUsoMensual(unCliente));
        restricciones.add(this.restriccionDeConsumoMensual(unCliente));

        PointValuePair resultados;
        try {
            resultados = new SimplexSolver().optimize(
                new MaxIter(100),
                funcionEconomica,
                new LinearConstraintSet(restricciones),
                GoalType.MAXIMIZE,
                new NonNegativeConstraint(true)
            );
        }
        catch (NoFeasibleSolutionException e) {
            throw new ClienteNoRecomendableException("No hay recomendación posible para los dispositivos de este cliente.");
        }

        int indice = 0;
        for (Dispositivo dispositivo : unCliente.getDispositivos()) {
            recomendaciones.put(dispositivo, resultados.getPoint()[indice]);
            indice++;
        }

        return recomendaciones;
    }

    /**
     * Devuelve la restricción de consumo mensual. Es la restricción que tiene la forma:
     * kWh1 + kWh2 + ... + kWhn <= 612
     */
    public LinearConstraint restriccionDeConsumoMensual(Cliente unCliente) {
        return new LinearConstraint(
            unCliente.getDispositivos().stream().mapToDouble(d -> d.getKWHora()).toArray(),
            Relationship.LEQ,
            this.getRestriccionEnergetica()
        );
    }
}
