import React from "react";

import { render, screen } from "@testing-library/react";
import '@testing-library/jest-dom'

import App from "../../App";


test('Render application, and check that sidebar is also rendered', () => {
  render(<App/>);
  const  sidebarElement = screen.getByTestId('sidebar');
  expect(sidebarElement).toBeInTheDocument();
});

//TODO: Implement further testing of functionality of sidebar, a bit tricky in react so i will create a new issue for this.
