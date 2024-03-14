package src;

public interface Constraint {
    public void Solve(double deltaT);

    public boolean InBroadCheck(double deltaT);
}
