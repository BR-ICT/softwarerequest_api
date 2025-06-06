import React from 'react';
import { shallow } from 'enzyme';
import PrintPOCancelPage from './PrintPOCancelPage';

describe('<PrintPOCancelPage />', () => {
  test('renders', () => {
    const wrapper = shallow(<PrintPOCancelPage />);
    expect(wrapper).toMatchSnapshot();
  });
});
