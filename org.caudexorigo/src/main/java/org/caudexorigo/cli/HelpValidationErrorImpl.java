/*
 * Copyright 2007 Tim Wood
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.caudexorigo.cli;

import org.caudexorigo.cli.ArgumentValidationException.ValidationError;

/**
 * The user requested help
 * 
 * @author tim
 */
class HelpValidationErrorImpl implements ValidationError {
  private final OptionsSpecification m_specification;

  /**
   * The user requested help
   * 
   * @param specification The options specification
   */
  public HelpValidationErrorImpl(final OptionsSpecification specification) {
    m_specification = specification;
  }

  /**
   * {@inheritdoc}
   */
  public ErrorType getErrorType() {
    return ErrorType.HelpRequested;
  }

  /**
   * {@inheritdoc}
   */
  public String getMessage() {
    return m_specification.toString();
  }

  /**
   * {@inheritdoc}
   */
  @Override
  public String toString() {
    return getMessage();
  }
}
