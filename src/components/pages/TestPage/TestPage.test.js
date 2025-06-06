import React from "react";
import { shallow } from "enzyme";
import TestPage from "./TestPage";

describe("<TestPage />", () => {
  test("renders", () => {
    const wrapper = shallow(<TestPage />);
    expect(wrapper).toMatchSnapshot();
  });
});
