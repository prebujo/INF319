y = [0.996694514502394	0.996731429490379	0.996764615403191	0.993188473446908	0.996680193468406	0.996694964586639	0.996732660003317	0.995603039160572	0.996752574766895	0.996742454531374	0.996723722121926	0.996162529258496	0.9965288000579	0.996711979542294	0.996761485982306	0.996727798926339	0.996689867195775	0.996771090050486	0.994263098289938	0.99661550601537	0.99667413423608	0.9967691925498	0.99552671934814	0.996746091794272	0.996716790980918	0.996739964152162	0.996034729573545	0.996687031860306	0.996687634471208	0.996749933548268	0.996716698450354	0.996694492731052	0.996750813825892	0.993061032969572	0.996761253959224	0.996605509268743	0.996756449298875	0.995231676437615	0.996707246627775	0.996696823399478	0.996761235700453	0.99596910158528	0.996749603013795	0.99671034798801	0.996755339237453	0.996752464702267	0.996631920002232	0.996738867608881	0.993044038983823	0.9965531840302	0.996689692936266	0.996724409606793	0.995266793132197	0.996529413985014	0.996671662414133	0.996803402935846	0.99591623690046	0.996700059769559	0.996728764398799	0.996708273460062	0.996636588506815	0.996723777654077	0.996691126579594	0.99413902999268	0.996618050631643	0.99658034118547	0.996753442179226	0.995595752353988	0.996590862219141	0.996733800610146	0.996734027669586	0.996225949163911	0.996753427944221	0.996757090872876	0.996808744781773	0.996724718561153	0.996709624340587	0.996764903644701	0.993229056644281	0.996733718412944	0.996669419710277	0.996681012721727	0.995527216787255	0.996619976237481	0.996716814419901	0.996786441227679	0.996163132139211	0.996687902122356	0.996719669732916	0.996774724242446	0.996657929600793	0.996694303256393	0.996720040730603	0.993286465177661	0.99668403247781	0.99660273538359	0.996747149823316	0.995662734292176	0.996800394875921	0.996726756382692	0.996745454069105	0.996124030555736	0.996651030501786	0.996722069142407	0.996777650060485	0.996650332969426	0.996649195058491	0.996765299614093	0.993898959527724	0.996727184090268	0.996652889976107	0.996740824698478	0.995752700655712	0.996527053320182	0.996681746915655	0.996753369959711	0.99607323532784	0.996634344121422	0.996631966106172	0.99670916375811]';

%1-way
rm_rnd_ins_first = [0	0	0	0	0	0	0	1	1	1	1	1	1	1	1	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1];
rm_non_clust_ins_clust = [0	0	0	1	1	1	1	0	0	0	0	1	1	1	1	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1];
rm_worst_ins_greedy = [0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1];
rm_shaw_ins_regret = [1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1];

%2-way
swap_and_rand=[	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1];
swap_and_clust=[	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1];
swap_and_greedy=[	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1];
swap_and_regret=[	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1];
exch_and_rand=[	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1];
exch_and_clust=[	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1];
exch_and_greedy	=[0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1];
exch_and_regret=[	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1];
two_opt_and_rand	=[0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1];
two_opt_and_clust=[	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1];
two_opt_and_greedy=[	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1];
two_opt_and_regret=[	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1];

p = anovan(y,{rm_rnd_ins_first,rm_non_clust_ins_clust,rm_worst_ins_greedy,rm_shaw_ins_regret,swap_and_rand,swap_and_clust,swap_and_greedy,swap_and_regret,exch_and_rand,exch_and_clust,exch_and_greedy,exch_and_regret,two_opt_and_rand,two_opt_and_clust,two_opt_and_greedy,two_opt_and_regret},'varnames',{'rm_rand_ins_first','rm_non_clust_ins_clust','rm_worst_ins_greedy','rm_shaw_ins_regret','swap_and_rand','swap_and_clust','swap_and_greedy','swap_and_regret','exch_and_rand','exch_and_clust','exch_and_greedy','exch_and_regret','2_opt_and_rand','2_opt_and_clust','2_opt_and_greedy','2_opt_and_regret'});

rm_rnd_ins_first=	rm_rnd_ins_first(:);
rm_non_clust_ins_clust=	rm_non_clust_ins_clust(:);
rm_worst_ins_greedy=	rm_worst_ins_greedy(:);
rm_shaw_ins_regret=	rm_shaw_ins_regret(:);
swap_and_rand = swap_and_rand(:);
swap_and_clust=	swap_and_clust(:);
swap_and_greedy=	swap_and_greedy(:);
swap_and_regret	=swap_and_regret(:);
exch_and_rand = exch_and_rand(:);
exch_and_clust	=exch_and_clust(:);
exch_and_greedy	=exch_and_greedy(:);
exch_and_regret	=exch_and_regret(:);
two_opt_and_rand = two_opt_and_rand(:);
two_opt_and_clust	=two_opt_and_clust(:);
two_opt_and_greedy=	two_opt_and_greedy(:);
two_opt_and_regret=	two_opt_and_regret(:);

X = [rm_rnd_ins_first,rm_non_clust_ins_clust,rm_worst_ins_greedy,rm_shaw_ins_regret,swap_and_rand,swap_and_clust,swap_and_greedy,swap_and_regret,exch_and_rand,exch_and_clust,exch_and_greedy,exch_and_regret,two_opt_and_rand,two_opt_and_clust,two_opt_and_greedy,two_opt_and_regret];


reg = fitlm(X,y, 'linear','ResponseVar','Best_Improvement','PredictorVars',{'rm_rand_ins_first','rm_non_clust_ins_clust','rm_worst_ins_greedy','rm_shaw_ins_regret','swap_and_rand','swap_and_clust','swap_and_greedy','swap_and_regret','exch_and_rand','exch_and_clust','exch_and_greedy','exch_and_regret','2_opt_and_rand','2_opt_and_clust','2_opt_and_greedy','2_opt_and_regret'})
