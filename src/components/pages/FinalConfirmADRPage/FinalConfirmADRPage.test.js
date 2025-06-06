import React from 'react';
import { shallow } from 'enzyme';
import FinalConfirmADRPage from './FinalConfirmADRPage';

describe('<FinalConfirmADRPage />', () => {
  test('renders', () => {
    const wrapper = shallow(<FinalConfirmADRPage />);
    expect(wrapper).toMatchSnapshot();
  });
});
