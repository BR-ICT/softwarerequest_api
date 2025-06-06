import React from "react";
import { shallow } from "enzyme";
import NewSWRPage from "./OperatorPage";

describe("<OperatorPage />", () => {
  test("renders", () => {
    const wrapper = shallow(<OperatorPage />);
    expect(wrapper).toMatchSnapshot();
  });
});
