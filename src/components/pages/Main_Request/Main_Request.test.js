import React from "react";
import { shallow } from "enzyme";
import FilePage from "./Main_Request";

describe("<FilePage />", () => {
  test("renders", () => {
    const wrapper = shallow(<FilePage />);
    expect(wrapper).toMatchSnapshot();
  });
});
