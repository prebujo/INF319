param nn;
param nf;
param nv;

#Section of sets
set N = 1 .. (2 * nn) ;
set N_p = 1 .. nn;
set N_d = nn + 1 .. (2 * nn);
set F = 1 .. nf;
set N_f {f in F};
set V = 1 .. nv;
set S;
set N_v {v in V};
set N_vp {v in V};

#Section of parameters
param o_v {v in V};
param d_v {v in V};
param C {v in V, s in S};
param C_fix {i in N_p};
param l {i in N, v in V};

#Section of variables
var d {v in V, s in S};
var y {i in N_p};
var x {i in N, j in N, v in V};

#objective function
minimize Total_cost: sum{v in V, s in S} C[v,s] * d[v,s] + sum{i in N_p} C_fix[i] * y[i];

#constraints
subject to picked_up {i in N_p}: sum {v in V, j in N_v[v]} x[i,j,v] + y[i] = 1;

subject to origin_dest {v in V, i in N_v[v]: i <> o_v[v] && i <> d_v[v]}:
	sum {j in N_v[v]} x[i,j,v] + sum {j in N_v[v]} x[j, i, v] = 0; 

subject to origin {v in V}: sum {j in N_v} x[o_v[v], j];

subject to dest {v in V}: sum {j in N_v} x[j, d_v[v]};

subject to delivery {v in V, i in N_vp[v]}: sum {j in N_v[v]} x[i,j,v] + sum {j in N_v[v]} x[i+n, j, v] = 0;

subject to weight_pickup {v in V, j in N_vp[v]}: l[i, v] + Q[j] - l[j,v] <= K[v] * (1 - x[i, j, v];  

subject to weight_delivery {v in V, j in N_vp[v]}: l[i, v] - Q[j] - l[j,v] <= K[v] * (1 - x[i, j, v];