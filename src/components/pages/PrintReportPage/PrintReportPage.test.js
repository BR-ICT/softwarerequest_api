import React from 'react';
import { shallow } from 'enzyme';
import PrintReportPage from './PrintReportPage';

describe('<PrintReportPage />', () => {
  test('renders', () => {
    const wrapper = shallow(<PrintReportPage />);
    expect(wrapper).toMatchSnapshot();
  });
});
