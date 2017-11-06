package com.depression.base.wechat.entity.menu;

public class ComplexButton extends Button
{
	private Button[] sub_button;

	public Button[] getSub_button()
	{
		return this.sub_button;
	}

	public void setSub_button(Button[] sub_button)
	{
		this.sub_button = sub_button;
	}

	public ComplexButton(Button[] sub_button)
	{
		this.sub_button = sub_button;
	}

	public ComplexButton()
	{
	}
}
