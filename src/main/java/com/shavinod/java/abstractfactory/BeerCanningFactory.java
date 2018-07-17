package com.shavinod.java.abstractfactory;

public class BeerCanningFactory extends CanningFactory {

	@Override
	public CanTop createTop() {
		// TODO Auto-generated method stub
		return new BeerCanTop();
	}

	@Override
	public CanBody createBody() {
		// TODO Auto-generated method stub
		return new BeerCanBody();
	}

}
