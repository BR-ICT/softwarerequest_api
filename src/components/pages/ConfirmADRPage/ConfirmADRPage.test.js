import React from "react";
import { shallow } from "enzyme";
import ConfirmADRPage from "./ConfirmADRPage";

describe("<ConfirmADRPage />", () => {
  test("renders", () => {
    const wrapper = shallow(<ConfirmADRPage />);
    expect(wrapper).toMatchSnapshot();
  });
});
