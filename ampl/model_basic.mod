set V;
set S;
set N;
set N_p;
set N_d;
set F;
set N_f;


param C {v in V} {s in S};
param C {i in N_p};

var d {v in V} {s in S};
var y {i in N_p};

minimize Total_cost: sum{v in V}{s in S} C[v][s] * d[v][s] + sum{i in N_p} C[i] * y[i];

