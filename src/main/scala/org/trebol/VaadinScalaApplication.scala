package org.trebol

import vaadin.scala._

class VaadinScalaApplication extends Application("Vaadin Scala project") {
	override val main = new VerticalLayout {
		margin = true
		components += Label("This Vaadin app uses Scaladin!")
	}

}