import React from 'react';
import { shallow } from 'enzyme';
import PlanMARPage from './PlanMARPage';

describe('<PlanMARPage />', () => {
  test('renders', () => {
    const wrapper = shallow(<PlanMARPage />);
    expect(wrapper).toMatchSnapshot();
  });
});
