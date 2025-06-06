import React from 'react';
import { shallow } from 'enzyme';
import ConfirmM3Page from './ConfirmM3Page';

describe('<ConfirmM3Page />', () => {
  test('renders', () => {
    const wrapper = shallow(<ConfirmM3Page />);
    expect(wrapper).toMatchSnapshot();
  });
});
