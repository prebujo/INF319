%T-test Cluster vs no cluster (no observations of combo cluster-group and
%cluster alone

All_Avrg=[3444.67188380109	149692.387513491	10287.7197437962	21640.5136249959	34212.9245401292	2500.95677561186	5987.83548548474	14412.6480942022	25212.6876087558	37799.405351971	1403.98444414147	5862.47598033238	6323.06772361021	12608.1776307381	19959.4059509425	1696.0857504857	434722.424956873	4615.08502631903	15629.4872971629	22727.042953562	5154.88919448056	3716.18137569454	13137.7516790025	23952.0299041105	41079.6975091981];
Selected_Avrg=[	3444.67188380109	149692.387513491	10323.149827616	21170.4904140998	34479.1117917334	2500.95677561186	5987.83548548474	14382.3495725741	25736.5748822994	36927.2293054977	1403.98444414147	5862.47598033238	6334.66092131205	12608.9680516394	19771.8741531385	1696.0857504857	434722.424956873	4652.84013579825	15539.9978085007	22508.599432368	5154.88919448056	3716.18137569454	13138.8510072062	24034.0318793115	41343.4391951043];
All_Best=[3444.67188380109	149692.387513491	9859.28333059428	20722.6881489762	32427.4639930627	2500.95677561186	5987.83548548474	14313.6310973521	24493.064649415	36197.8214650655	1403.98444414147	5862.47598033238	6281.27859168376	11901.920362092	19409.1377010411	1696.0857504857	434722.424956873	4505.88692213699	15332.3867755105	22242.0066424502	5154.88919448056	3716.18137569454	12974.4976379488	23702.1103823323	39308.4847427087];
Selected_Best=[	3444.67188380109	149692.387513491	9997.89682411049	20911.5382135082	32797.9585644961	2500.95677561186	5987.83548548474	14272.0811801665	24760.7993001726	35932.1222680442	1403.98444414147	5862.47598033238	6267.69922783916	12347.5612137838	19149.8440396399	1696.0857504857	434722.424956873	4494.51996673036	15290.5532394437	22252.5544837708	5154.88919448056	3716.18137569454	12944.1597284413	23855.5414802898	39847.0377762406];

[h_a,     p_a,  ci_a,     stats_a]      = ttest(All_Avrg,Selected_Avrg,0.1,'both')
[h_a_r,   p_a_r,ci_a_r,   stats_a_r]    = ttest(All_Avrg,Selected_Avrg,0.1,'right')
[h_a_l,   p_a_l,ci_a_l,   stats_a_l]    = ttest(All_Avrg,Selected_Avrg,0.1,'left')

[h_b,     p_b,  ci_b,     stats_b]      = ttest(All_Best,Selected_Best,0.05,'both')
[h_b_r,   p_b_r,ci_b_r,   stats_b_r]    = ttest(All_Best,Selected_Best,0.05,'right')
[h_b_l,   p_b_l,ci_b_l,   stats_b_l]    = ttest(All_Best,Selected_Best,0.05,'left')