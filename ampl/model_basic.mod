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
set N_vnf{v in V, f in F} = {n in N_v[v]: n not in N_f[f]};
set N_vp {v in V};
set E {v in V} = {i in N,j in N: j <> i};
set P {i in N};


#Section of parameters
param o_v {v in V};
param d_v {v in V};
param C {v in V, s in S};
param C_fix {i in N_p};
param K {v in V};
param H {f in F};
param Q {i in N};
param pi {i in N};
param T_l {i in N, p in P[i]};
param T_u {i in N, p in P[i]};
param T {i in N, j in N, v in V};
param D {i in N, j in N};


#Section of variables
var d {v in V, s in S} >= 0;
var y {i in N_p} >= 0;
var x {i in N, j in N, v in V} >= 0;
var l {i in N, v in V} >= 0;
var h {i in N} >= 0;
var u {i in N, p in P[i]} >=0;
var t {i in N} >= 0;

#objective function
minimize Total_cost: sum{v in V, s in S} C[v,s] * d[v,s] + sum{i in N_p} C_fix[i] * y[i];

#constraints
subject to picked_up {i in N_p}: sum {v in V, j in N_v[v]} x[i,j,v] + y[i] = 1;

subject to origin_dest {v in V, i in N_v[v]: i <> o_v[v] && i <> d_v[v]}:
	sum {j in N_v[v]} x[i,j,v] + sum {j in N_v[v]} x[j, i, v] = 0; 

subject to origin {v in V}: sum {j in N_v[v]} x[o_v[v], j, v] = 1;

subject to dest {v in V}: sum {j in N_v[v]} x[j, d_v[v], v] = 1;

subject to delivery {v in V, i in N_vp[v]}: sum {j in N_v[v]} x[i,j,v] + sum {j in N_v[v]} x[i+nn, j, v] = 0;

subject to weight_pickup {v in V, j in N_vp[v], (i,j) in E[v]}: l[i, v] + Q[j] - l[j,v] <= K[v] * (1 - x[i, j, v]);  

subject to weight_delivery {v in V, j in N_vp[v], (i,j+nn) in E[v]}: l[i, v] - Q[j] - l[j,v] <= K[v] * (1 - x[i, j, v]);

subject to weight_limits {v in V, i in N_vp[v]}: l[i,v] <= K[v];

#TODO: check if hub space is in order and test with different instances
subject to hub_inside {v in V, f in F, i in N_f[f], j in N_f[f]}: 
	h[i] + 1 - h[j] <= H[f] * (1 - x[i,j,v]);

subject to hub_limit {v in V, f in F, j in N_f[f]}:
	h[j] <= H[f] * sum {i in N_v[v]} x[i,j,v];

subject to hub_enter {v in V, f in F, j in N_f[f]}: 
	h[j] >= sum {i in N_v[v]: i not in N_f[f]} x[i,j,v];

subject to timewindow_sum {i in N}: sum{p in P[i]} u[i,p] = 1;

subject to timewindow_lower {i in N}: sum{p in P[i]} u[i, p] * T_l[i, p] <= t[i];

subject to timewindow_upper {i in N}:  sum{p in P[i]} u[i,p] * T_u[i, p] >= t[i];

subject to time_travel {v in V, (i,j) in E[v]}: 
	t[i] + T[i,j,v] - t[j] <= (T_u[i,pi[i]] + T[i,j,v]) * (1 - x[i,j,v]);
	
subject to structure_distance {v in V}: sum {s in S} d[v,s] = sum {(i,j) in E[v]} x[i,j,v] * D[i,j];
	
	
	





 