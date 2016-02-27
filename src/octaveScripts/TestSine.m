%
% Written by Federico O'Reilly Regueiro for COPM 5541, calculator project
% Winter 2016
% 
% This script declares two functions corrsponding to the Chebyshev and Taylor 
% polynomial approximations of sine as well as an auxiliary function that takes
% a function handle as an argument so as to call the two approximations and 
% octave's sine function in order to calculate and plot the absolute and 
% relative errors.
% 
% The source of the Chebyshev approximation by Charles K. Garret from 2012 
% can be found in the following paper: 
% http://krisgarrett.net/papers/l2approx.pdf

1; % tell octave this is a script file and not a function file

% Now declare functions, first, a function to approximate sine of a given input
% in radians.
function result = mycheby(x)
  % left these commented out, TODO change for 17th order -pi/2 to pi/2
  % 21th order from -pi to pi
  % coefficients = zeros(11,1);
  % coefficients(1) = 1.00000000000000098216e+00;
  % coefficients(2) = - 1.66666666666674074058e-01;
  % coefficients(3) = 8.33333333334987771150e-03;
  % coefficients(4) = - 1.98412698429672570320e-04;
  % coefficients(5) = 2.75573193196855760359e-06;
  % coefficients(6) = - 2.50521116230089813913e-08;
  % coefficients(7) = 1.60591122567208977895e-10;
  % coefficients(8) = - 7.64807134493815932275e-13;
  % coefficients(9) = 2.81875350346861226633e-15;
  % coefficients(10) = - 8.53932287916564238231e-18;
  % coefficients(11) = 2.47852306233493974115e-20;
  %15th order -pi/2 to pi/2
  % vector (array) of chebyshev coefficients, 8 rows, 1 column
  coefficients = zeros(8,1);
  % notice octave is 1-based indexing, as opposed to 0-based in Java
  coefficients(1) = 9.99999999999999312880e-01;
  coefficients(2) = - 1.66666666666652434127e-01;
  coefficients(3) = 8.33333333324810981519e-03;
  coefficients(4) = - 1.98412698184898843225e-04;
  coefficients(5) = 2.75573160083833319909e-06;
  coefficients(6) = - 2.50518516666250910087e-08;
  coefficients(7) = 1.60473922487682573331e-10;
  coefficients(8) = - 7.36644541924532787403e-13;
  
  % exponents. Declaring a range for a vector goes: [start:step:end]
  exponents = [1:2:2*length(coefficients)-1];
  % repeat the vector so that the matrix multiplication corresponds to what
  % we'd get by looping:
  exponents = repmat(exponents, length(x), 1 );
  
  % x is our input argument (vector) indexing thus (:) ensures it is m-by-1
  % as opposed to 1-by-m
  x = x(:);
  % - print-style debugging, ending a statement without a colon prints answer
  size(x) % TODO comment out when done
  x = repmat(x, 1, size(exponents(1)) );
  % .^ (or .* or ./) carries operation in a point-wise fashion for matrices that 
  % have equal dimensions
  x = x.^exponents;
  % we declared function result = mycheby(x) so result is what we'll return
  result = x * coefficients; 
endfunction

% Now declare a taylor series function 
function result = mytaylor(x)
  exponents = [1:2:15];
  facs = 1./(factorial(exponents));
  signs = (-ones(1,length(exponents))).^((exponents-1)/2);
  facs = facs(:) .* signs(:);
  exponents = repmat(exponents, length(x), 1 );
  
  x = x(:);
  x = repmat(x, 1, size(exponents(1)) );
  x = x.^exponents;
  
  result = x * facs;
endfunction

% this function helps us unify the script, just pass the vector and tell it
% which function to call
function result = MySine(angle, fun, isRadians = true)
% We can feed degrees or radians to the function, radians by default
  if (isRadians)
  % surest branch first... do nothing
  else
    angle = (angle / 360.0) * 2 * Pi; % go with degrees or radians? this is in between
  endif
  % wrestle the angle into the -pi pi range
  temp = sign(angle);
  offset = abs(angle)/(pi);
  offset = 2*round(offset/2);
  angle -= temp .* offset * pi;
  
  % our aproximation is best from -pi/2 to +pi/2
  % use the symmetry of sine and calculate only 2 quadrants around 0
  if (angle < -pi/2)
    result = feval(fun, -pi - angle);
  else
    if (angle < pi/2) 
      result = feval(fun, angle);
    else % (Pi/2 < angle < pi)
      result = feval(fun, pi - angle);
    endif
  endif
endfunction  

% Finally! this part is the script, after all those function declarations
domain = [-2*pi: 0.005: 2*pi];
chebRange = MySine(domain, @mycheby);
taylorRange = MySine(domain, @mytaylor);
sinRange = MySine(domain, @sin);
% Absolute differences
diffCh = sinRange(:) - chebRange(:);
diffTay = sinRange(:) - taylorRange(:);
% relative differences
relDiffCh = abs(diffCh(:)./sinRange(:))-1;
% get rid of non-numeric values that mess up the plot
relDiffCh = relDiffCh(:) .* isfinite(relDiffCh)(:);
relDiffTay = abs(diffTay(:)./sinRange(:))-1;
% Don't know why but this value is huge!, suppress it for plotting
relDiffCh(1) = 0;
relDiffTay(1) = 0; 
relDiffTay = relDiffTay(:) .* isfinite(relDiffTay)(:); 
% now all the plotting
% invoke one figure window
figure (1)
% plot domain against resulting range
plot(domain, chebRange);
% Name the plot
title("Approximation of sine function");
% hold on allows us to keep plotting without erasing previous plots
hold on;
% now plot another one in green ('g')
plot(domain, taylorRange, 'g');
% another in red...
plot (domain, sinRange, 'r');
% legend has as many strings or char sequences as plots in a figure and prints 
% a legend in the figure correlating strings to each plot.
legend('15th order chebyshev approximation','15th order taylor approximation','octave sine function')
hold off
% octave has some plotting bug that was making it crash, pausing between figures 
% solved it
pause (1)
% repeat...
figure (2)
% semilogy is like plot but displays y axis in logarithmic scale
semilogy(domain, diffCh)
hold on;
semilogy(domain, diffTay, 'g')
title('Absolute errors of approximations relative to the octave library sine function');
legend('Chebyshev 15th order error', 'Talor 15th order error' );
hold off
pause (1)
figure (3)
semilogy(domain, relDiffCh)
hold on;
semilogy(domain, relDiffTay, 'g')
title('Relative errors of approximations against the octave library sine function');
legend('Chebyshev 15th order error', 'Talor 15th order error' );
hold off
disp "ok, bye" % print just so we know we're done