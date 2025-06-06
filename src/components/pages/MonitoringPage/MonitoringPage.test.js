import React from 'react';
import { shallow } from 'enzyme';
import MonitoringPage from './MonitoringPage';

describe('<MonitoringPage />', () => {
  test('renders', () => {
    const wrapper = shallow(<MonitoringPage />);
    expect(wrapper).toMatchSnapshot();
  });
});
