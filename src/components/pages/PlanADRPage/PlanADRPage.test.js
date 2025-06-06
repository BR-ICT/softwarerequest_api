import React from 'react';
import { shallow } from 'enzyme';
import PlanADRPage from './PlanADRPage';

describe('<PlanADRPage />', () => {
  test('renders', () => {
    const wrapper = shallow(<PlanADRPage />);
    expect(wrapper).toMatchSnapshot();
  });
});
