import React from 'react';
import { shallow } from 'enzyme';
import FilePage from './FilePage';

describe('<FilePage />', () => {
  test('renders', () => {
    const wrapper = shallow(<FilePage />);
    expect(wrapper).toMatchSnapshot();
  });
});
