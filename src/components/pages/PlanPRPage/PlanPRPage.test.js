import React from 'react';
import { shallow } from 'enzyme';
import PlanPRPage from './PlanPRPage';

describe('<PlanPRPage />', () => {
  test('renders', () => {
    const wrapper = shallow(<PlanPRPage />);
    expect(wrapper).toMatchSnapshot();
  });
});
