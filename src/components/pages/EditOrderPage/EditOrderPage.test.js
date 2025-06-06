import React from 'react';
import { shallow } from 'enzyme';
import EditOrderPage from './EditOrderPage';

describe('<EditOrderPage />', () => {
  test('renders', () => {
    const wrapper = shallow(<EditOrderPage />);
    expect(wrapper).toMatchSnapshot();
  });
});
