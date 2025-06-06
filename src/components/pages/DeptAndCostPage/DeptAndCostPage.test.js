import React from 'react';
import { shallow } from 'enzyme';
import DeptAndCostPage from './DeptAndCostPage';

describe('<DeptAndCostPage />', () => {
  test('renders', () => {
    const wrapper = shallow(<DeptAndCostPage />);
    expect(wrapper).toMatchSnapshot();
  });
});
