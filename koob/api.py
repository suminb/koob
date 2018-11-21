from flask import Blueprint, jsonify, request

from koob.forms import ReservationForm
from koob.models import RecurringFrequency, Reservation, Resource
from koob.utils import parse_date


api_module = Blueprint('api', __name__)


@api_module.route('/v1/resources')
def resources_v1():
    resources = Resource.query.all()
    return jsonify({'resources': resources})


@api_module.route('/v1/reservations')
def reservations_v1():
    date = parse_date(request.args.get('date', '2018-11-21'))

    return jsonify({
        'reservations': Reservation.find_reservations_on(date).all()
    })


@api_module.route('/v1/reservations', methods=['POST'])
def create_reservation_v1():
    form = ReservationForm(request.form)
    if form.validate():
        reservation = Reservation.create(**{
            key: getattr(form, key).data for key in [
                'resource_id', 'starts_at', 'ends_at', 'reserved_by',
                'title', 'description',
                'recurring_frequency', 'recurring_count']
        })
        return jsonify(reservation)
    else:
        return jsonify(form.errors), 400
