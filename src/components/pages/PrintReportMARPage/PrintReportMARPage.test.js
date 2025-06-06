import React from 'react';
import { shallow } from 'enzyme';
import PrintReportMARPage from './PrintReportMARPage';

describe('<PrintReportMARPage />', () => {
  test('renders', () => {
    const wrapper = shallow(<PrintReportMARPage />);
    expect(wrapper).toMatchSnapshot();
  });
});
