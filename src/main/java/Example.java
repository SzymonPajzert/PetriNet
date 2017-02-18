import petrify.snoopy.parser.package$;

import petrify.snoopy.parser.ParseResult;

import petrify.model.PetriNet;
import petrify.model.State;
import petrify.model.Place;

import scala.collection.JavaConverters;

import java.util.Collection;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;

import java.lang.Iterable;
import java.util.stream.Collectors;
import java.util.LinkedList;

public class Example {

	private static boolean isInteresting(Place place) {
		return place.name().equals("AAs") || place.name().equals("ABCA1");
	}
	

	public static void main(String[] args) throws Exception {
		File initialFile = new File("./file.spept");
		InputStream targetStream = new FileInputStream(initialFile);

		System.out.println("Parsing started");
		ParseResult result = package$.MODULE$.tupleToParseResult(package$.MODULE$.read(targetStream));

		PetriNet net = result.net();
		State state = result.state();

		System.out.println("Starting state: ");
		System.out.println(state);
		System.out.println("");

		Collection<State> nextStates = JavaConverters.asJavaCollectionConverter(net.iterate(state)).asJavaCollection();

		Collection<Place> allPlaces = JavaConverters.asJavaCollectionConverter(state.places()).asJavaCollection();

		System.out.println(allPlaces.size());
		
		Collection<Place> interestingPlaces = allPlaces.stream()
			.filter(p -> Example.isInteresting(p))
			.collect(Collectors.toCollection(LinkedList::new));

		System.out.println("Interesujące miejsca - liczba: " + interestingPlaces.size());
		System.out.println(net.apply(state));
		System.out.println("Koniec");
	}

}
