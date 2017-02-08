import petrify.snoopy.parser.package$;

import petrify.snoopy.parser.ParseResult;

import petrify.model.PetriNet;
import petrify.model.State;

import java.io.File;

public class Example {

	public static void main(String[] args) {
		String file = "./file.spept";

		ParseResult result = package$.MODULE$.tupleToParseResult(package$.MODULE$.read(file));

		PetriNet net = result.net();
		State state = result.state();

		System.out.println(state);

	}

}
