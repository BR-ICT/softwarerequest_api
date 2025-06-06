import React from 'react';
import { shallow } from 'enzyme';
import ReservPRReportPage from './ReservPRReportPage';

describe('<ReservPRReportPage />', () => {
  test('renders', () => {
    const wrapper = shallow(<ReservPRReportPage />);
    expect(wrapper).toMatchSnapshot();
  });
});
