y = [6422128.60039654	21431.0974773029	21634.79388622	21333.3851838586	50577.6703304915	23676.7872548657	23768.2732176621	22146.0444293824	29792.1435865847	23231.9082900936	22816.9264414115	21767.8385551764	26232.6355307761	24319.5245143318	21697.6701755702	22319.6465864772	6422128.60039654	21553.1871638179	21640.5175083005	21387.5199453262	47101.5518118303	23552.3863172017	24508.8556839205	22739.5351329658	30221.2839458189	24582.2606815504	22279.3051708796	23220.6992553838	26632.7624505085	23527.9559476597	23680.5835984415	22060.1194987679	6422128.60039654	21743.5023780385	22167.3891020355	22048.855439185	50435.8530492199	23208.8531164948	22359.992504537	22413.6427272573	34452.4390352671	22435.1661284691	22405.6149836409	22938.7058623692	27273.7489158492	24349.9865159626	22513.2901291583	22795.2896993835	6422128.60039654	21580.4087314647	22662.9893747425	21439.1592857856	50515.6073103603	23555.2325579943	23358.7702716662	22687.5805571362	33158.7574878431	24870.4745604264	22050.8967824736	22032.5756266096	28380.7111015482	23482.8168526781	23597.1871758785	21886.9364372004	6422128.60039653	25110.9378944447	22674.2680432289	22314.3130909015	45022.563787924	23661.5520034411	22619.0410317115	23407.9970992601	30055.6569141302	23257.2041507357	21747.477692509	21600.9572676263	26006.8535274982	22717.4051860197	21734.9448544684	21994.6899809454	6422128.60039653	23732.1660962334	22134.6226599267	21596.8815522252	48877.6000413344	25971.3104054409	22554.4148270774	24134.2649694029	30892.8730177454	24002.0042831778	22164.5841228236	23987.301183324	26458.1974424998	23083.1295614093	23673.4503311741	22646.9901660816	6422128.60039653	24257.8710559037	22908.6539855277	21937.921115303	49283.4057022069	23273.186352942	24341.3398777465	23464.5090187538	29828.1146269107	23524.0634669559	22973.0752627352	22808.490016884	27707.2321398399	22342.0607757593	23130.1112011956	21721.8101384307	6422128.60039653	22479.5789226122	23131.536566618	22418.1939170962	49150.7023686025	23674.1364645175	24252.825319747	23981.2683296619	29912.2028612746	23870.6592396919	24246.8491392402	22305.9233163202	27188.3593792219	22696.898012743	23677.5016152218	23196.3738440182]';
swap_first = [0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1];
swap = swap_first(:);
exch_3 = [0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1];
exch = exch_3(:);
two_opt = [0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1];
twoOpt = two_opt(:);
rm_rnd_ins_first = [0	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1	0	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1	0	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1	0	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1	0	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1	0	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1	0	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1	0	0	0	0	0	0	0	0	1	1	1	1	1	1	1	1];
rm_rnd = rm_rnd_ins_first(:);
rm_non_clust_ins_clust = [0	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1	0	0	0	0	1	1	1	1];
rm_non_clust = rm_non_clust_ins_clust(:);
rm_worst_ins_greedy = [0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1	0	0	1	1];
rm_worst = rm_worst_ins_greedy(:);
rm_shaw_ins_regret = [0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1	0	1];
rm_shaw = rm_shaw_ins_regret(:);
X = [swap, exch, twoOpt, rm_rnd, rm_non_clust, rm_worst, rm_shaw ];

p = anovan(y,{swap_first,exch_3,two_opt,rm_rnd_ins_first,rm_non_clust_ins_clust,rm_worst_ins_greedy,rm_shaw_ins_regret},'varnames',{'swap_first','exch_3','two_opt','rm_rand_ins_first','rm_non_clust_ins_clust','rm_worst_ins_greedy','rm_shaw_ins_regret'});
p2 = anovan(y,{swap_first,exch_3,two_opt,rm_rnd_ins_first,rm_non_clust_ins_clust,rm_worst_ins_greedy,rm_shaw_ins_regret},'model','interaction','varnames',{'swap_first','exch_3','two_opt','rm_rand_ins_first','rm_non_clust_ins_clust','rm_worst_ins_greedy','rm_shaw_ins_regret'});
reg = fitlm(X,y, 'linear', 'ResponseVar','Average_Objective','PredictorVars',{'swap','exchange','2-opt','rm_rand_ins_first', 'rm_non_clust_ins_clust','rm_worst_ins_greedy','rm_shaw_ins_regret'})