import React from 'react';
import { shallow } from 'enzyme';
import SendReportPage from './SendReportPage';

describe('<SendReportPage />', () => {
  test('renders', () => {
    const wrapper = shallow(<SendReportPage />);
    expect(wrapper).toMatchSnapshot();
  });
});
