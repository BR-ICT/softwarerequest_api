import React from "react";
import { shallow } from "enzyme";
import ReturnPage from "./ReturnPage";

describe("<ReturnPage />", () => {
  test("renders", () => {
    const wrapper = shallow(<ReturnPage />);
    expect(wrapper).toMatchSnapshot();
  });
});
