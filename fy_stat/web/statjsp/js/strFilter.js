function chSM(inStr){
	re=/[a-zA-Z0-9_-]$/;
	if (re.test(inStr)==false){
		return false;
	}
	return true;
}
function chSMX(inStr){
	re=/[a-z0-9_-]$/;
	if (re.test(inStr)==false){
		return false;
	}
	return true;
}
function chZH(inStr){
	re=/[\u3447-\uFA29]/;
	if (re.test(inStr)==false){
		return false;
	}
	return true;
}
function chND(inStr){
	re=/[0-9.]$/;
	if (re.test(inStr)==false){
		return false;
	}
	return true;
}
function chNX(inStr){
	re=/^[0-9]*[\*]*$/;
	if (re.test(inStr)==false){
		return false;
	}
	return true;
}
function chNU(inStr){
	re=/[0-9]$/;
	if (re.test(inStr)==false){
		return false;
	}
	return true;
}
function chSX(inStr){
	re=/[a-zA-Z_]$/;
	if (re.test(inStr)==false){
		return false;
	}
	return true;
}
function chSC(inStr){
	re=/[a-zA-Z]$/;
	if (re.test(inStr)==false){
		return false;
	}
	return true;
}
function chSD(inStr){
	re=/[A-Z]$/;
	if (re.test(inStr)==false){
		return false;
	}
	return true;
}
function chSXI(inStr){
	re=/^[A-Z]*[\*]*$/
	if (re.test(inStr)==false){
		return false;
	}
	return true;
}
function chSS(inStr){
	re=/[a-z]$/;
	if (re.test(inStr)==false){
		return false;
	}
	return true;
}
function chSN(inStr){
	re=/[a-zA-Z0-9]$/;
	if (re.test(inStr)==false){
		return false;
	}
	return true;
}
function chNZ(inStr){
	re=/[0-9\u3447-\uFA29]/;
	if (re.test(inStr)==false){
		return false;
	}
	return true;
}
function chSZ(inStr){
	re=/[a-zA-Z\u3447-\uFA29]/;
	if (re.test(inStr)==false){
		return false;
	}
	return true;
}
function chSH(inStr){
	re=/[-]*[A-Z]$/;
	if (re.test(inStr)==false){
		return false;
	}
	return true;
}
function chSZN(inStr){
	re=/[a-zA-Z0-9\u3447-\uFA29]/;
	if (re.test(inStr)==false){
		return false;
	}
	return true;
}

