import React from 'react';
import { shallow } from 'enzyme';
import PrintPRReservReportPage from './PrintPRReservReportPage';

describe('<PrintPRReservReportPage />', () => {
  test('renders', () => {
    const wrapper = shallow(<PrintPRReservReportPage />);
    expect(wrapper).toMatchSnapshot();
  });
});
