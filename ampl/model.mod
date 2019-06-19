#Parameters for amounts
#amount of factories
param nf;

#amount of Vehicles
param nv;

#amount of orders
param no;

#amount of nodes
param nn=no+nv;

#amount of locations
param nl;

#Amount of distinktive stops
param ns = nl;

#Section of sets
set N = 1 .. (2 * nn) ;
set N_p = 1 .. nn;
set N_d = nn + 1 .. (2 * nn);
set O = 1 .. no;
set F = 1 .. nf;
set N_f {f in F};
set V = 1 .. nv;
set S = 1 .. ns;
set L{s in S};
set L_pd{o in O};
set L_dd{o in O};
set N_v {v in V};
set N_vp {v in V};
set E {v in V} = {i in N_v[v],j in N_v[v]: j <> i};
param pi {i in N};
set P {i in N} = 1 .. pi[i];

#length of cost structure distance dimention
param na{v in V};

#Length of cost structure wegiht dimention
param nb{v in V};

#Cost structure sets
set Alpha{v in V} = 1 .. na[v];
set Alpha_zero{v in V} = 0 .. na[v];
set Beta{v in V} = 1 .. nb[v];
set Beta_zero{v in V} = 0 .. nb[v];
set A{v in V} = {alp in Alpha[v], bet in Beta[v]};

#Section of parameters
param o_v {v in V};
param d_v {v in V};
param C_km {v in V, (alp,bet) in A[v]};
param C_kg {v in V, (alp,bet) in A[v]};
param C_fix {v in V, (alp,bet) in A[v]};
param C_stop {v in V, i in N};
param C {i in N_p};
param K_kg {v in V};
param K_vol {v in V};
param H {f in F};
param Q_kg {i in N_p};
param Q_vol {i in N_p};
param T_l {i in N, p in P[i]};
param T_u {i in N, p in P[i]};
param T {v in V, (i, j) in E[v]};
param D {i in N, j in N};
param B {v in V, alp in Alpha_zero[v]};
param Z {v in V, bet in Beta_zero[v]};

#Section of variables
var d {v in V, (alp,bet) in A[v]} >= 0;
var y {i in N_p} binary;
var x {i in N, j in N, v in V:i<>j} binary;
var l_kg {i in N, v in V} >= 0;
var l_vol {i in N, v in V} >= 0;
var l_max {v in V, (alp, bet) in A[v]} >= 0;
var h {i in N} >= 0;
var u {i in N, p in P[i]} binary;
var t {i in N} >= 0;
var b {v in V, (alp,bet) in A[v]} binary;

#objective function
minimize Total_cost: 
sum{v in V, (alp,bet) in A[v]} (C_km[v,alp,bet] * d[v,alp,bet] + C_kg[v, alp, bet]*l_max[v, alp, bet] + C_fix[v, alp, bet]*b[v, alp, bet]) + 
sum{v in V, s in S, i in L[s], j in N_v[v]:j not in L[s]} C_stop[v,i]*x[i,j,v] +
sum{i in N_p} C[i] * y[i];

#constraints
subject to picked_up_or_not {i in N_p}: 
	sum {v in V, j in N_v[v]:j<>i&&i in N_v[v]} x[i,j,v] + y[i] = 1;

subject to enter_exit {v in V, i in N_v[v]: i <> o_v[v] && i <> d_v[v]}:
	sum {j in N_v[v]: j<> i} x[i,j,v] - sum {j in N_v[v]:j <> i} x[j, i,v] = 0; 

subject to origin {v in V}: 
	sum {j in N_v[v]:j<>o_v[v]} x[o_v[v], j, v] = 1;

subject to destination {v in V}: 
	sum {j in N_v[v]: j<> d_v[v]} x[j, d_v[v], v] = 1;

subject to delivery {v in V, i in N_vp[v]:i<> o_v[v]}: 
	sum {j in N_v[v]:j <> i} x[i,j,v] - sum {j in N_v[v]:j<>(i+nn)} x[(i+nn), j, v] = 0;

subject to weight_pickup {v in V, j in N_vp[v], (i,j) in E[v]}: 
	l_kg[i, v] + Q_kg[j] - l_kg[j,v] <= K_kg[v] * (1 - x[i, j, v]);  

subject to weight_delivery {v in V, j in N_vp[v], (i,(j+nn)) in E[v]}: 
	l_kg[i, v] - Q_kg[j] - l_kg[(j+nn),v] <= K_kg[v] * (1 - x[i, (j+nn), v]);

subject to weight_limits {v in V, i in N_vp[v]}: 
	l_kg[i,v] <= K_kg[v];
	
subject to volume_pickup {v in V, j in N_vp[v], (i,j) in E[v]}: 
	l_vol[i, v] + Q_vol[j] - l_vol[j,v] <= K_vol[v] * (1 - x[i, j, v]); 
	
subject to volume_delivery {v in V, j in N_vp[v], (i,(j+nn)) in E[v]}: 
	l_vol[i, v] - Q_vol[j] - l_vol[(j+nn),v] <= K_vol[v] * (1 - x[i, (j+nn), v]);

subject to volume_limits {v in V, i in N_vp[v]}: 
	l_vol[i,v] <= K_vol[v];

subject to factory_inside {v in V, f in F, i in N_f[f], j in N_f[f]: i<>j}: 
	h[i] + 1 - h[j] <= (H[f] + 1) * (1 - x[i,j,v]);

subject to factory_limit {v in V, f in F, j in N_f[f]}:
	h[j] <= H[f];

subject to factory_enter {v in V, f in F, j in N_f[f]}: 
	h[j] >= sum {i in N_v[v]: i not in N_f[f]} x[i,j,v];

subject to timewindow_sum {i in N}: 
	sum{p in P[i]} u[i,p] = 1;

subject to timewindow_lower {i in N}: 
	sum{p in P[i]} u[i, p] * T_l[i, p] <= t[i];

subject to timewindow_upper {i in N}:  
	sum{p in P[i]} u[i,p] * T_u[i, p] >= t[i];

subject to time_travel {v in V, (i,j) in E[v]}: 
	t[i] + T[v,i,j] - t[j] <= (T_u[i,pi[i]] + T[v,i,j]) * (1 - x[i,j,v]);

subject to time_at_pickup {v in V, i in N_vp[v]}:
	t[i] + T[v,i,(i+nn)] - t[(i+nn)] <= 0;
	
subject to total_distance {v in V}: 
	sum {(alp,bet) in A[v]} d[v,alp,bet] = sum {(i,j) in E[v]} x[i,j,v] * D[i,j];
	
subject to maximum_weight {v in V, i in N_v[v]}:
	sum {(alp,bet) in A[v]} l_max[v, alp, bet] >= l_kg[i, v];

subject to distance_interval_lower{v in V, (alp, bet) in A[v]}:
	d[v, alp, bet] >= B[v,alp-1]*b[v, alp, bet];
	
subject to distance_interval_upper{v in V, (alp, bet) in A[v]}:
	d[v, alp, bet] <= B[v,alp]*b[v, alp, bet];	
	
subject to weight_interval_lower{v in V, (alp, bet) in A[v]}:
	l_max[v,alp,bet] >= Z[v,bet-1]*b[v, alp, bet];
	
subject to weight_interval_upper{v in V, (alp, bet) in A[v]}:
	l_max[v,alp,bet] <= Z[v,bet]*b[v, alp, bet];
	
subject to matrix_selection{v in V}:
	sum{(alp,bet) in A[v]} b[v,alp,bet] <= sum{j in N_v[v]: j<>o_v[v]&&j<>d_v[v]} x[o_v[v],j,v];
	
	
	





 