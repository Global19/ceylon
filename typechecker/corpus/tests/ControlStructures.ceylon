class ControlStructures {
	
	class ForFail {
	
		List<String> names = { "Gavin", "Emmanuel", "Andrew", "Jason" };
		Map<String,String> fullNames = { "Gavin"->"King", "Jason"->"Greene" };
		
		for (String name in names) {
			log.info(name);
		}
		
		for (String name in names) {
			if (name=="Gavin") {
				log.info("found");
				break true;
			}
			fail {
				log.info("not found");
			}
		}
		
		for (String first->String last in fullNames) {
			log.info("${first} ${last}");
		}
		
	}
	
	class IfElse {
	
		if (true) {
			log.info("always true");
		}
	
		if (false) {
			log.info("never true");
		}
		else {
			log.info("always false");
		}
		
		optional String hello = "Hello";
		
		if (exists hello) {
			log.info(hello);
		}
		
		optional String s = null;
		
		if (exists s) {
			log.info(s);
		}
		else {
			log.info("never exists");
		}
		
		class Foo {}
		class Bar extends Foo {}
		
		Foo foo = Bar();
		
		if (is Bar foo) {
			log.info("it is a Bar");
		}
		else {
			log.info("it is not a Bar");
		}
		
		optional List<Foo> foos = { Foo(), Bar() };
		
		if (nonempty foos) {
			log.info($foos);
		}
		else {
			log.info("empty");
		}
		
	}
	
	class DoWhile {
		
		while (true) {
			log.info("hello");
		}
		
		do {
			log.info("hello");
		}
		while (true);
		
		do {
			log.info("hello");
		}
		while (true) {
			log.info("goodbye");
		}
		
		do (Iterator<Character> tokens = "Hello World!".tokens().iterator) 
		while (tokens.more)
		{
			if ("hello"==tokens.next().lowercase) {
				log.info("found");
				break;
			}
		}
		
	}
	
	class TryCatchFinally {
		
		try ( Transaction() ) {
			doSomething();
		}
		
		try {
			throw Exception();
		}
		catch (Exception e) {
			retry;
		}
		
		try ( Transaction(), Session s = Session() ) {
			throw Exception();
		}
		catch (Exception e) {
			throw e;
		}
		
		try ( Session s = Session() ) {
			throw Exception();
		}
		catch (Exception e) {
			log.error(e);
		};
		
		try {
			doSomething();
		}
		catch (FooException fe) {
			throw fe;
		}
		catch (BarException be) {
			retry;
		}
		
		try {
			log.info("entering");
			throw Exception();
		}
		finally {
			log.info("exiting");
		}
		
		try {
			log.info("entering");
			throw Exception();
		}
		catch (Exception e) {
			log.error("catching");
		}
		finally {
			log.info("exiting");
		}
		
	}
	
	class SwitchCaseElse {
		
		Number n = 1;
		switch(n)
		case (is Natural) {
			log.info("natural");
		}
		case (is Float) {
			log.info("float");
		}
		case (is Integer) {
			log.info("integer");
		}
		case (is Whole) {
			log.info("whole");
		}
		case (is Decimal) {
			log.info("decimal");
		}
		else {
			log.info("unknown");
		}
		
		Natural m = 0;
		switch(m)
		case (0) {
			log.info("Zero");
		}
		case (1) {
			log.info("Unity");
		}
		else {
			log.info($m);
		}
		
		class Color { case red, case green, case blue; }
		Color c = red;
		switch(c)
		case (red) {
			log.info("red");
		}
		case (green,blue) {
			log.info("never anything else");
		}
		
		optional Color cc = null;
		switch (cc) 
		case (null) {
			log.info("unknown color");
		}
		case (red, green, blue) {
			log.info("known color ${cc}");
		}
		
	}

}