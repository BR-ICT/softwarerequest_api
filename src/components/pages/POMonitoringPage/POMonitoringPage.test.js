import React from 'react';
import { shallow } from 'enzyme';
import POMonitoringPage from './POMonitoringPage';

describe('<POMonitoringPage />', () => {
  test('renders', () => {
    const wrapper = shallow(<POMonitoringPage />);
    expect(wrapper).toMatchSnapshot();
  });
});
