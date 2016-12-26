package petrify.snoopy.parser

import org.scalatest.FunSuite

import petrify.snoopy.model._

class SnoopyParserTest extends FunSuite {

  val invitro : scala.xml.Node = <Snoopy revision="0.98c2-BETA" version="2">
    <netclass name="Petri Net"/>
    <nodeclasses count="5">
      <nodeclass count="2" name="Place">
        <node id="77706" net="1">
          <attribute id="77707" name="Name" net="1">
            <![CDATA[R|S1]]>
          </attribute>
          <attribute id="77708" name="ID" net="1">
            <![CDATA[0]]>
          </attribute>
          <attribute id="77709" name="Marking" net="1">
            <![CDATA[0]]>
          </attribute>
          <attribute id="77710" name="Logic" net="1">
            <![CDATA[0]]>
          </attribute>
          <attribute id="77711" name="Comment" net="1">
            <![CDATA[]]>
          </attribute>
        </node>
        <node id="77739" net="1">
          <attribute id="77740" name="Name" net="1">
            <![CDATA[S1]]>
          </attribute>
          <attribute id="77741" name="ID" net="1">
            <![CDATA[1]]>
          </attribute>
          <attribute id="77742" name="Marking" net="1">
            <![CDATA[1]]>
          </attribute>
          <attribute id="77743" name="Logic" net="1">
            <![CDATA[0]]>
          </attribute>
          <attribute id="77744" name="Comment" net="1">
            <![CDATA[MAX 1000000000]]>
          </attribute>
        </node>
      </nodeclass>
      <nodeclass count="1" name="Transition">
        <node id="77825" net="1">
          <attribute id="77826" name="Name" net="1">
            <![CDATA[k3]]>
          </attribute>
          <attribute id="77827" name="ID" net="1">
            <![CDATA[0]]>
          </attribute>
          <attribute id="77828" name="Logic" net="1">
            <![CDATA[0]]>
          </attribute>
          <attribute id="77829" name="Comment" net="1">
            <![CDATA[]]>
          </attribute>
          <attribute id="82547" name="EFT" net="1">
            <![CDATA[?]]>
          </attribute>
          <attribute id="82549" name="LFT" net="1">
            <![CDATA[?]]>
          </attribute>
          <attribute id="82551" name="Duration" net="1">
            <![CDATA[]]>
          </attribute>
        </node>
      </nodeclass>
    </nodeclasses>

    <edgeclasses count="1">
      <edgeclass count="2" name="Edge">
        <edge id="77948" net="1" source="77825" target="77739">
          <attribute id="102848" name="Equation" net="1">
            <![CDATA[1]]>
          </attribute>
          <attribute id="77949" name="Multiplicity" net="1">
            <![CDATA[1]]>
          </attribute>
          <attribute id="77950" name="Comment" net="1">
            <![CDATA[]]>
          </attribute>
        </edge>
        <edge id="77954" net="1" source="77706" target="77825">
          <attribute id="102860" name="Equation" net="1">
            <![CDATA[1]]>
          </attribute>
          <attribute id="77955" name="Multiplicity" net="1">
            <![CDATA[1]]>
          </attribute>
          <attribute id="77956" name="Comment" net="1">
            <![CDATA[]]>
          </attribute>
        </edge>
      </edgeclass>
    </edgeclasses>
  </Snoopy>

  val parser = SnoopyParser(invitro)

  val (places, transitions, edges) = parser.parse

  test("testTransitions") {
    //TODO
  }

  test("testEdges") {
    //TODO
  }

  test("testPlaces") {
    val RS1 = Place(77706, "R|S1", 3)
    val S1 = Place(77739, "S1", 1000000000)
    assert(places == Iterable(RS1, S1) || places == Iterable(S1, RS1))
  }

  // TODO test of maximum capacity
}
