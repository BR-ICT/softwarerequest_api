import React from "react";
import { shallow } from "enzyme";
import NewSWRPage from "./VisitorPage";

describe("<VisitorPage />", () => {
  test("renders", () => {
    const wrapper = shallow(<VisitorPage />);
    expect(wrapper).toMatchSnapshot();
  });
});
