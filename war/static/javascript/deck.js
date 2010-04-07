function showhide(id) {
    // Bug somewhere. Please fix!
    if(document.getElementById(id).style.display == "none" || document.getElementById(id).style.display == null) {
        document.getElementById(id).style.display = "block";
    } else {
        document.getElementById(id).style.display = "none";
    }
}
	
	
	// configuration for autocard links via javascript
	var gathererBaseURL = "gatherer.wizards.com";
	var expCodeLookup = {
	    CON: "Conflux",
	    ALA: "Shards of Alara",
	    EVE: "Eventide",
	    SHA: "Shadowmoor",
	    MOR: "Morningtide",
	    LRW: "Lorwyn",
	    FUT: "Future Sight",
	    PLC: "Planar Chaos",
	    TSB: "Time Spiral \"Timeshifted\"",
	    TSP: "Time Spiral",
	    DIS: "Dissension",
	    GPT: "Guildpact",
	    RAV: "Ravnica",
	    SOK: "Saviors of Kamigawa",
	    BOK: "Betrayers of Kamigawa",
	    CHK: "Champions of Kamigawa",
	    "5DN": "Fifth Dawn",
	    DST: "Darksteel",
	    MRD: "Mirrodin",
	    SCG: "Scourge",
	    LGN: "Legions",
	    ONS: "Onslaught",
	    JUD: "Judgment",
	    TOR: "Torment",
	    OD: "Odyssey",
	    AP: "Apocalypse",
	    PS: "Planeshift",
	    IN: "Invasion",
	    PR: "Prophecy",
	    NE: "Nemesis",
	    MM: "Mercadian Masques",
	    CG: "Urza's Destiny",
	    GU: "Urza's Legacy",
	    UZ: "Urza's Saga",
	    EX: "Exodus",
	    ST: "Stronghold",
	    TE: "Tempest",
	    WL: "Weatherlight",
	    VI: "Visions",
	    MI: "Mirage",
	    AL: "Alliances",
	    CSP: "Coldsnap",
	    IA: "Ice Age",
	    HM: "Homelands",
	    FE: "Fallen Empires",
	    DK: "The Dark",
	    LE: "Legends",
	    AQ: "Antiquities",
	    AN: "Arabian Nights",
	    "10E": "Tenth Edition",
	    "9ED": "Ninth Edition",
	    "8ED": "Eighth Edition",
	    "7E": "Seventh Edition",
	    "6E": "Classic Sixth Edition",
	    "5E": "Fifth Edition",
	    "4E": "Fourth Edition",
	    "3E": "Revised Edition",
	    "2U": "Unlimited Edition",
	    "2E": "Limited Edition Beta",
	    "1E": "Limited Edition Alpha"
	};
	
	function autoCardWindow(cardname) {  
	    agent = navigator.userAgent;
	    windowName = "Sitelet";
	    params  = "";
	    params += "toolbar=1,";
	    params += "location=1,";
	    params += "directories=0,";
	    params += "status=0,";
	    params += "menubar=0,";
	    params += "scrollbars=1,";
	    params += "resizable=1,";
	    params += "width=800,";
	    params += "height=670";
	
	    var useName = cardname.replace(/_/g, " ");
	    useName = useName.replace(/\]/g, "&");
	    useName = useName.replace(/\[/g, "'");
	
	    win = window.open("http://ww2.wizards.com/Gatherer/CardDetails.aspx?name="+cardname, windowName , params);
	    
	};
