package com.xuanzhi.tools.ds;
import java.util.HashMap;
import java.util.Random;

import junit.framework.TestCase;

public class Int2IntMapTestCase2 extends TestCase{

	public static String testData[] = new String[]{
		"object_tracker_create","403385981",
		"object_tracker_create","402841448",
		"object_tracker_create","402595460",
		"object_tracker_create","407238548",
	
		"object_tracker_create","395603505",
		"object_tracker_create","396880136",
		"object_tracker_create","396886578",
		"object_tracker_create","396088040",
		"object_tracker_create","399887322",
		"object_tracker_create","400017261",
		"object_tracker_create","399090231",
		"object_tracker_create","399615567",
		"object_tracker_create","399325633",
		"object_tracker_create","400664962",
		"object_tracker_create","404003844",
		"object_tracker_create","403946892",
		"object_tracker_create","403561448",
		"object_tracker_create","403571176",
		"object_tracker_create","403140402",
		"object_tracker_create","403894140",
		"object_tracker_destroy","393654087",
		"object_tracker_destroy","399334250",
		"object_tracker_destroy","402841448",
		"object_tracker_destroy","402595460",
		"object_tracker_destroy","407238548",
		"object_tracker_destroy","397684373",
		"object_tracker_destroy","395603505",
		"object_tracker_destroy","396880136",
		"object_tracker_destroy","396886578",
		"object_tracker_destroy","396088040",
		"object_tracker_destroy","399887322",
		"object_tracker_destroy","400017261",
		"object_tracker_destroy","399090231",
		"object_tracker_destroy","399615567",
		"object_tracker_destroy","399325633",
		"object_tracker_destroy","400664962",
		"object_tracker_destroy","404003844",
		"object_tracker_destroy","403571176",
		"object_tracker_create","395632254",
		"object_tracker_create","393666241",
		"object_tracker_create","394000154",
		"object_tracker_create","394482721",
		"object_tracker_create","394912949",
		"object_tracker_create","394916084",
		"object_tracker_create","398313178",
		"object_tracker_create","398383622",
		"object_tracker_create","398168455",
		"object_tracker_create","398172196",
		"object_tracker_create","398175210",
		"object_tracker_create","400805218",
		"object_tracker_create","400836246",
		"object_tracker_create","402006323",
		"object_tracker_create","405582705",
		"object_tracker_destroy","403946892",
		"object_tracker_destroy","403561448",
		"object_tracker_destroy","403140402",
		"object_tracker_destroy","403894140",
		"object_tracker_destroy","395632254",
		"object_tracker_destroy","394000154",
		"object_tracker_destroy","394482721",
		"object_tracker_destroy","394912949",
		"object_tracker_destroy","394916084",
		"object_tracker_destroy","398313178",
		"object_tracker_destroy","398383622",
		"object_tracker_destroy","398168455",
		"object_tracker_destroy","400805218",
		"object_tracker_create","380335802",
		"object_tracker_create","396157277",
		"object_tracker_create","396530043",
		"object_tracker_create","397204944",
		"object_tracker_create","397445136",
		"object_tracker_create","400122984",
		"object_tracker_create","402350267",
		"object_tracker_create","402353537",
		"object_tracker_create","402356649",
		"object_tracker_create","402366161",
		"object_tracker_create","402631254",
		"object_tracker_destroy","400836246",
		"object_tracker_destroy","402006323",
		"object_tracker_destroy","405582705",
		"object_tracker_destroy","380335802",
		"object_tracker_destroy","396157277",
		"object_tracker_destroy","396530043",
		"object_tracker_destroy","397204944",
		"object_tracker_destroy","397445136",
		"object_tracker_destroy","400122984",
		"object_tracker_destroy","402356649",
		"object_tracker_destroy","402366161",
		"object_tracker_create","395409152",
		"object_tracker_create","395434624",
		"object_tracker_create","395446602",
		"object_tracker_create","393019223",
		"object_tracker_create","397766133",
		"object_tracker_create","397197884",
		"object_tracker_create","398899595",
		"object_tracker_create","398502238",
		"object_tracker_create","398533881",
		"object_tracker_create","400027131",
		"object_tracker_create","401944680",
		"object_tracker_create","401093589",
		"object_tracker_create","401097018",
		"object_tracker_create","402575450",
		"object_tracker_create","406191294",
		"object_tracker_create","406267690",
		"object_tracker_create","407416565",
		"object_tracker_create","407456724",
		"object_tracker_destroy","402350267",
		"object_tracker_destroy","402353537",
		"object_tracker_destroy","393666241",
		"object_tracker_destroy","398172196",
		"object_tracker_destroy","395434624",
		"object_tracker_destroy","395446602",
		"object_tracker_destroy","393019223",
		"object_tracker_destroy","397766133",
		"object_tracker_destroy","397197884",
		"object_tracker_destroy","398899595",
		"object_tracker_destroy","398502238",
		"object_tracker_destroy","398533881",
		"object_tracker_destroy","400027131",
		"object_tracker_destroy","401944680",
		"object_tracker_destroy","401093589",
		"object_tracker_destroy","401097018",
		"object_tracker_destroy","402575450",
		"object_tracker_create","395195134",
		"object_tracker_create","397304793",
		"object_tracker_create","397564541",
		"object_tracker_create","397572940",
		"object_tracker_create","397441745",
		"object_tracker_create","401161677",
		"object_tracker_create","402030056",
		"object_tracker_create","402090173",
		"object_tracker_create","403024070",
		"object_tracker_create","403037089",
		"object_tracker_destroy","395101782",
		"object_tracker_destroy","406191294",
		"object_tracker_destroy","407416565",
		"object_tracker_destroy","407456724",
		"object_tracker_destroy","395195134",
		"object_tracker_destroy","397564541",
		"object_tracker_destroy","397572940",
		"object_tracker_destroy","401161677",
		"object_tracker_destroy","402030056",
		"object_tracker_destroy","402090173",
		"object_tracker_create","389438681",
		"object_tracker_create","389579266",
		"object_tracker_create","389593631",
		"object_tracker_create","384062796",
		"object_tracker_create","396589336",
		"object_tracker_create","396709926",
		"object_tracker_create","398467243",
		"object_tracker_create","398749751",
		"object_tracker_create","399545136",
		"object_tracker_create","401299565",
		"object_tracker_create","401304128",
		"object_tracker_create","404128997",
		"object_tracker_create","403390452",
		"object_tracker_destroy","403024070",
		"object_tracker_destroy","403037089",
		"object_tracker_destroy","395409152",
		"object_tracker_destroy","389579266",
		"object_tracker_destroy","389593631",
		"object_tracker_destroy","396709926",
		"object_tracker_destroy","398467243",
		"object_tracker_destroy","398749751",
		"object_tracker_destroy","401299565",
		"object_tracker_destroy","401304128",
		"object_tracker_create","395491085",
		"object_tracker_create","395516153",
		"object_tracker_create","394478008",
		"object_tracker_create","397872293",
		"object_tracker_create","399008907",
		"object_tracker_create","399247380",
		"object_tracker_create","399250681",
		"object_tracker_create","397954693",
		"object_tracker_create","401595959",
		"object_tracker_create","401599158",
		"object_tracker_create","402658650",
		"object_tracker_create","402663988",
		"object_tracker_create","402128685",
		"object_tracker_destroy","389438681",
		"object_tracker_destroy","399545136",
		"object_tracker_destroy","404128997",
		"object_tracker_destroy","403390452",
		"object_tracker_destroy","397304793",
		"object_tracker_destroy","395491085",
		"object_tracker_destroy","395516153",
		"object_tracker_destroy","394478008",
		"object_tracker_destroy","397872293",
		"object_tracker_destroy","399008907",
		"object_tracker_destroy","399247380",
		"object_tracker_destroy","399250681",
		"object_tracker_destroy","397954693",
		"object_tracker_destroy","401595959",
		"object_tracker_destroy","401599158",
		"object_tracker_destroy","402663988",
		"object_tracker_create","380014024",
		"object_tracker_create","395541399",
		"object_tracker_create","394468681",
		"object_tracker_create","397384859",
		"object_tracker_create","396219389",
		"object_tracker_create","398741437",
		"object_tracker_create","398533391",
		"object_tracker_create","398458711",
		"object_tracker_create","398752883",
		"object_tracker_create","400733135",
		"object_tracker_create","400740086",
		"object_tracker_create","400776231",
		"object_tracker_create","401931132",
		"object_tracker_create","400885149",
		"object_tracker_create","402104027",
		"object_tracker_create","402030849",
		"object_tracker_create","404760130",
		"object_tracker_create","403637527",
		"object_tracker_create","406422058",
		"object_tracker_create","406506736",
		"object_tracker_create","407395956",
		"object_tracker_destroy","397441745",
		"object_tracker_destroy","402658650",
		"object_tracker_destroy","402128685",
		"object_tracker_destroy","398175210",
		"object_tracker_destroy","380014024",
		"object_tracker_destroy","395541399",
		"object_tracker_destroy","394468681",
		"object_tracker_destroy","397384859",
		"object_tracker_destroy","396219389",
		"object_tracker_destroy","398741437",
		"object_tracker_destroy","398533391",
		"object_tracker_destroy","398458711",
		"object_tracker_destroy","400733135",
		"object_tracker_destroy","400740086",
		"object_tracker_destroy","400776231",
		"object_tracker_destroy","401931132",
		"object_tracker_destroy","400885149",
		"object_tracker_destroy","402104027",
		"object_tracker_destroy","402030849",
		"object_tracker_create","395148314",
		"object_tracker_create","395617567",
		"object_tracker_create","394080607",
		"object_tracker_create","395298334",
		"object_tracker_create","396378384",
		"object_tracker_create","397976946",
		"object_tracker_create","397139024",
		"object_tracker_create","396595155",
		"object_tracker_create","398537890",
		"object_tracker_create","400369621",
		"object_tracker_create","402107903",
		"object_tracker_create","401709532",
		"object_tracker_create","401304851",
		"object_tracker_create","403095659",
		"object_tracker_create","404782913",
		"object_tracker_create","404792113",
		"object_tracker_create","406524453",
		"object_tracker_destroy","398752883",
		"object_tracker_destroy","404760130",
		"object_tracker_destroy","403637527",
		"object_tracker_destroy","406422058",
		"object_tracker_destroy","406506736",
		"object_tracker_destroy","407395956",
		"object_tracker_destroy","406267690",
		"object_tracker_destroy","395148314",
		"object_tracker_destroy","395617567",
		"object_tracker_destroy","394080607",
		"object_tracker_destroy","395298334",
		"object_tracker_destroy","396378384",
		"object_tracker_destroy","397976946",
		"object_tracker_destroy","397139024",
		"object_tracker_destroy","398537890",
		"object_tracker_destroy","400369621",
		"object_tracker_destroy","402107903",
		"object_tracker_destroy","401304851",
		"object_tracker_destroy","403095659",
		"object_tracker_create","380154703",
		"object_tracker_create","393548833",
		"object_tracker_create","395311996",
		"object_tracker_create","388098021",
		"object_tracker_create","388102246",
		"object_tracker_create","397463366",
		"object_tracker_create","396489738",
		"object_tracker_create","396493084",
		"object_tracker_create","398290127",
		"object_tracker_create","398298106",
		"object_tracker_create","399157942",
		"object_tracker_create","399140905",
		"object_tracker_create","399486699",
		"object_tracker_create","400728278",
		"object_tracker_create","400799735",
		"object_tracker_create","402032592",
		"object_tracker_create","404989900",
		"object_tracker_create","406223319",
		"object_tracker_destroy","404782913",
		"object_tracker_destroy","404792113",
		"object_tracker_destroy","380154703",
		"object_tracker_destroy","393548833",
		"object_tracker_destroy","395311996",
		"object_tracker_destroy","388102246",
		"object_tracker_destroy","397463366",
		"object_tracker_destroy","396489738",
		"object_tracker_destroy","396493084",
		"object_tracker_destroy","398290127",
		"object_tracker_destroy","398298106",
		"object_tracker_destroy","399157942",
		"object_tracker_destroy","399486699",
		"object_tracker_destroy","400728278",
		"object_tracker_destroy","400799735",
		"object_tracker_destroy","402032592",
		"object_tracker_create","392642265",
		"object_tracker_create","394082131",
		"object_tracker_create","393861786",
		"object_tracker_create","397307964",
		"object_tracker_create","398838897",
		"object_tracker_create","398850270",
		"object_tracker_create","398941341",
		"object_tracker_create","401730173",
		"object_tracker_create","401799065",
		"object_tracker_create","403137978",
		"object_tracker_create","406231710",
		"object_tracker_destroy","396595155",
		"object_tracker_destroy","388098021",
		"object_tracker_destroy","399140905",
		"object_tracker_destroy","404989900",
		"object_tracker_destroy","406223319",
		"object_tracker_destroy","392642265",
		"object_tracker_destroy","394082131",
		"object_tracker_destroy","397307964",
		"object_tracker_destroy","398941341",
		"object_tracker_destroy","401730173",
		"object_tracker_destroy","401799065",
		"object_tracker_create","393917160",
		"object_tracker_create","393941734",
		"object_tracker_create","393946530",
		"object_tracker_create","394706623",
		"object_tracker_create","393855604",
		"object_tracker_create","397084649",
		"object_tracker_create","397182043",
		"object_tracker_create","397185222",
		"object_tracker_create","398102437",
		"object_tracker_create","398147094",
		"object_tracker_create","398249554",
		"object_tracker_create","400137846",
		"object_tracker_create","400604074",
		"object_tracker_create","401006820",
		"object_tracker_create","400980816",
		"object_tracker_create","400894766",
		"object_tracker_create","400898954",
		"object_tracker_create","400907281",
		"object_tracker_create","404756530",
		"object_tracker_create","404764052",
		"object_tracker_create","405942594",
		"object_tracker_create","405214401",
		"object_tracker_destroy","398838897",
		"object_tracker_destroy","403137978",
		"object_tracker_destroy","406231710",
		"object_tracker_destroy","406524453",
		"object_tracker_destroy","393917160",
		"object_tracker_destroy","393941734",
		"object_tracker_destroy","393946530",
		"object_tracker_destroy","394706623",
		"object_tracker_destroy","397084649",
		"object_tracker_destroy","397182043",
		"object_tracker_destroy","398102437",
		"object_tracker_destroy","398147094",
		"object_tracker_destroy","398249554",
		"object_tracker_destroy","400137846",
		"object_tracker_destroy","400604074",
		"object_tracker_destroy","401006820",
		"object_tracker_destroy","400898954",
		"object_tracker_destroy","400907281",
		"object_tracker_destroy","404764052",
		"object_tracker_create","395062748",
		"object_tracker_create","398014437",
		"object_tracker_create","398031316",
		"object_tracker_create","398043706",
		"object_tracker_create","398054041",
		"object_tracker_create","398067004",
		"object_tracker_create","396360976",
		"object_tracker_create","400348137",
		"object_tracker_create","400366129",
		"object_tracker_create","400379602",
		"object_tracker_create","400405197",
		"object_tracker_create","400417537",
		"object_tracker_create","400441003",
		"object_tracker_create","401731759",
		"object_tracker_create","403464443",
		"object_tracker_create","402737065",
		"object_tracker_create","402743890",
		"object_tracker_create","402746995",
		"object_tracker_create","402488156",
		"object_tracker_destroy","400980816",
		"object_tracker_destroy","400894766",
		"object_tracker_destroy","404756530",
		"object_tracker_destroy","405942594",
		"object_tracker_destroy","405214401",
		"object_tracker_destroy","398850270",
		"object_tracker_destroy","401709532",
		"object_tracker_destroy","395062748",
		"object_tracker_destroy","398014437",
		"object_tracker_destroy","398031316",
		"object_tracker_destroy","398043706",
		"object_tracker_destroy","398054041",
		"object_tracker_destroy","396360976",
		"object_tracker_destroy","400348137",
		"object_tracker_destroy","400366129",
		"object_tracker_destroy","400379602",
		"object_tracker_destroy","400405197",
		"object_tracker_destroy","400417537",
		"object_tracker_destroy","403464443",
		"object_tracker_destroy","402488156",
		"object_tracker_create","386847707",
		"object_tracker_create","386850960",
		"object_tracker_create","387010161",
		"object_tracker_create","395466850",
		"object_tracker_create","390047028",
		"object_tracker_create","397086813",
		"object_tracker_create","397090237",
		"object_tracker_create","398023486",
		"object_tracker_create","398039859",
		"object_tracker_create","398049383",
		"object_tracker_create","397923797",
		"object_tracker_create","400652022",
		"object_tracker_create","401226050",
		"object_tracker_create","401228697",
		"object_tracker_create","401239350",
		"object_tracker_create","401261605",
		"object_tracker_create","401995348",
		"object_tracker_create","404171051",
		"object_tracker_create","404200830",
		"object_tracker_create","403588269",
		"object_tracker_create","403597811",
		"object_tracker_create","405535659",
		"object_tracker_destroy","398067004",
		"object_tracker_destroy","401731759",
		"object_tracker_destroy","402737065",
		"object_tracker_destroy","402746995",
		"object_tracker_destroy","393861786",
		"object_tracker_destroy","386847707",
		"object_tracker_destroy","386850960",
		"object_tracker_destroy","387010161",
		"object_tracker_destroy","395466850",
		"object_tracker_destroy","390047028",
		"object_tracker_destroy","397086813",
		"object_tracker_destroy","397090237",
		"object_tracker_destroy","398023486",
		"object_tracker_destroy","398039859",
		"object_tracker_destroy","398049383",
		"object_tracker_destroy","400652022",
		"object_tracker_destroy","401228697",
		"object_tracker_destroy","401995348",
		"object_tracker_create","395635709",
		"object_tracker_create","392577821",
		"object_tracker_create","393088325",
		"object_tracker_create","393856097",
		"object_tracker_create","396395419",
		"object_tracker_create","396370028",
		"object_tracker_create","399178196",
		"object_tracker_create","398790982",
		"object_tracker_create","400891934",
		"object_tracker_create","400896284",
		"object_tracker_create","400899365",
		"object_tracker_create","400590027",
		"object_tracker_create","400540402",
		"object_tracker_create","402368891",
		"object_tracker_create","402372261",
		"object_tracker_create","402375578",
		"object_tracker_create","402378711",
		"object_tracker_create","405000724",
		"object_tracker_create","407084982",
		"object_tracker_destroy","404171051",
		"object_tracker_destroy","404200830",
		"object_tracker_destroy","403588269",
		"object_tracker_destroy","403597811",
		"object_tracker_destroy","405535659",
		"object_tracker_destroy","400441003",
		"object_tracker_destroy","402743890",
		"object_tracker_destroy","395635709",
		"object_tracker_destroy","392577821",
		"object_tracker_destroy","393088325",
		"object_tracker_destroy","393856097",
		"object_tracker_destroy","396395419",
		"object_tracker_destroy","396370028",
		"object_tracker_destroy","399178196",
		"object_tracker_destroy","400896284",
		"object_tracker_destroy","400899365",
		"object_tracker_destroy","400590027",
		"object_tracker_destroy","400540402",
		"object_tracker_destroy","402378711",
		"object_tracker_destroy","405000724",
		"object_tracker_create","380344801",
		"object_tracker_create","380348163",
		"object_tracker_create","380384785",
		"object_tracker_create","393943333",
		"object_tracker_create","397219329",
		"object_tracker_create","396700950",
		"object_tracker_create","396545744",
		"object_tracker_create","396567184",
		"object_tracker_create","396683325",
		"object_tracker_create","398870342",
		"object_tracker_create","399138366",
		"object_tracker_create","399902442",
		"object_tracker_create","399905807",
		"object_tracker_create","405204987",
		"object_tracker_create","405192793",
		"object_tracker_create","406917374",
		"object_tracker_destroy","398790982",
		"object_tracker_destroy","400891934",
		"object_tracker_destroy","402368891",
		"object_tracker_destroy","402372261",
		"object_tracker_destroy","402375578",
		"object_tracker_destroy","407084982",
		"object_tracker_destroy","401226050",
		"object_tracker_destroy","380348163",
		"object_tracker_destroy","380384785",
		"object_tracker_destroy","393943333",
		"object_tracker_destroy","397219329",
		"object_tracker_destroy","396567184",
		"object_tracker_destroy","399138366",
		"object_tracker_destroy","405204987",
		"object_tracker_destroy","405192793",
		"object_tracker_create","384057457",
		"object_tracker_create","384405222",
		"object_tracker_create","393087712",
		"object_tracker_create","397160667",
		"object_tracker_create","396876121",
		"object_tracker_create","400788811",
		"object_tracker_create","400052113",
		"object_tracker_create","401478661",
		"object_tracker_create","401481790",
		"object_tracker_create","401484924",
		"object_tracker_create","401487945",
		"object_tracker_create","402636584",
		"object_tracker_create","406763034",
		"object_tracker_create","406851226",
		"object_tracker_create","406861182",
		"object_tracker_create","406871389",
		"object_tracker_create","406880428",
		"object_tracker_destroy","398870342",
		"object_tracker_destroy","399905807",
		"object_tracker_destroy","406917374",
		"object_tracker_destroy","401239350",
		"object_tracker_destroy","384057457",
		"object_tracker_destroy","384405222",
		"object_tracker_destroy","393087712",
		"object_tracker_destroy","397160667",
		"object_tracker_destroy","396876121",
		"object_tracker_destroy","400788811",
		"object_tracker_destroy","400052113",
		"object_tracker_destroy","401481790",
		"object_tracker_destroy","401484924",
		"object_tracker_destroy","401487945",
		"object_tracker_destroy","402636584",
		"object_tracker_create","389143234",
		"object_tracker_create","397171129",
		"object_tracker_create","396296169",
		"object_tracker_create","398416777",
		"object_tracker_create","398027926",
		"object_tracker_create","399942504",
		"object_tracker_create","399679490",
		"object_tracker_create","399682909",
		"object_tracker_create","399826961",
		"object_tracker_create","399830158",
		"object_tracker_create","400779366",
		"object_tracker_create","406378723",
		"object_tracker_create","406398364",
		"object_tracker_create","406411743",
		"object_tracker_create","405728018",
		"object_tracker_create","407836915",
		"object_tracker_destroy","401478661",
		"object_tracker_destroy","406851226",
		"object_tracker_destroy","406861182",
		"object_tracker_destroy","406871389",
		"object_tracker_destroy","406880428",
		"object_tracker_destroy","389143234",
		"object_tracker_destroy","397171129",
		"object_tracker_destroy","396296169",
		"object_tracker_destroy","398027926",
		"object_tracker_destroy","399942504",
		"object_tracker_destroy","399679490",
		"object_tracker_destroy","399682909",
		"object_tracker_destroy","399826961",
		"object_tracker_destroy","399830158",
		"object_tracker_destroy","406398364",
		"object_tracker_destroy","406411743",
		"object_tracker_create","386744511",
		"object_tracker_create","386970844",
		"object_tracker_create","391101045",
		"object_tracker_create","391104296",
		"object_tracker_create","391107987",
		"object_tracker_create","396997879",
		"object_tracker_create","396974524",
		"object_tracker_create","396978407",
		"object_tracker_create","399485708",
		"object_tracker_create","398440435",
		"object_tracker_create","398446987",
		"object_tracker_create","398454823",
		"object_tracker_create","407907296",
		"object_tracker_create","406272688",
		"object_tracker_create","403051743",
		"object_tracker_create","408025379",
		"object_tracker_create","407610722",
		"object_tracker_create","407368517",
		"object_tracker_create","402357879",
		"object_tracker_create","407911691",
		"object_tracker_create","407915042",
		"object_tracker_destroy","406378723",
		"object_tracker_destroy","405728018",
		"object_tracker_destroy","407836915",
		"object_tracker_destroy","386744511",
		"object_tracker_destroy","386970844",
		"object_tracker_destroy","391101045",
		"object_tracker_destroy","391104296",
		"object_tracker_destroy","391107987",
		"object_tracker_destroy","396997879",
		"object_tracker_destroy","396974524",
		"object_tracker_destroy","396978407",
		"object_tracker_destroy","399485708",
		"object_tracker_destroy","398440435",
		"object_tracker_destroy","398446987",
		"object_tracker_destroy","407907296",
		"object_tracker_destroy","406272688",
		"object_tracker_destroy","403051743",
		"object_tracker_destroy","408025379",
		"object_tracker_destroy","407610722",
		"object_tracker_destroy","407368517",
		"object_tracker_create","394745046",
		"object_tracker_create","390176654",
		"object_tracker_create","390192436",
		"object_tracker_create","396245774",
		"object_tracker_create","396249013",
		"object_tracker_create","397958711",
		"object_tracker_create","397658780",
		"object_tracker_create","399900912",
		"object_tracker_create","399963358",
		"object_tracker_create","399968510",
		"object_tracker_create","402920502",
		"object_tracker_create","402928729",
	};
	
	public void testA() throws Exception{
		Int2IntMap map = new Int2IntMap();
		
		for(int i = 0 ;i < testData.length/2 ; i++){
			String s = testData[2*i];
			int hashcode = Integer.valueOf(testData[2*i+1]);
			
			String ss = map.dump();
			if(s.equals("object_tracker_create")){
				map.put(hashcode, i);
			}
			if(map.check() == false ){
				System.out.println("-----------------before put error -----------");
				System.out.println(ss);
				System.out.println("-----------------put error -----------");
				System.out.println(map.dump());
				
				assertTrue(false);
			}
			
			if(hashcode == 395409152){
				System.out.println(map.dump());
			}
			
			if(s.equals("object_tracker_destroy")){
				boolean b = map.containsKey(hashcode);
				if(b == false){
					System.out.println("Error: misskey " + s + "," + testData[2*i+1]);
				}
				try{
					map.remove(hashcode);
					
					if(map.check() == false ){
						System.out.println("-----------------before remove("+hashcode+") error -----------");
						System.out.println(ss);
						System.out.println("-----------------remove error -----------");
						System.out.println(map.dump());
						
						assertTrue(false);
					}
					
				}catch(Exception e){
					System.out.println("Remove: internal error " + s + "," + testData[2*i+1]);
					
					
					throw e;
					
				}
			}
		}
	}
	
}
